package com.github.yangwk.weboot.common.configuration;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;

import com.github.yangwk.weboot.common.lock.DistributedLock;

@Configuration
public class DesignConstraintsConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public DesignConstraintsSupport designConstraintsSupport(ApplicationContext applicationContext) {
		DesignConstraintsFunction distributedLockFunc = (metadataReader, classLoader) -> {
			Set<MethodMetadata> methodMetadatas = metadataReader.getAnnotationMetadata()
					.getAnnotatedMethods(DistributedLock.class.getName());
			boolean checkScheduledAndAsync = false;
			for (MethodMetadata metadata : methodMetadatas) {
				if (checkScheduledAndAsync) {
					/**
					 * Can not annotation with {@link Scheduled} or {@link Schedules} or {@link Async} ,
					 * ensure acquire lock and task runnable in the current thread .
					 *
					 */
					if (metadata.isAnnotated(Scheduled.class.getName())
							|| metadata.isAnnotated(Schedules.class.getName())
							|| metadata.isAnnotated(Async.class.getName())) {
						return "the method " + metadata.getDeclaringClassName() + "." + metadata.getMethodName()
								+ " is annotated " + DistributedLock.class.getName()
								+ " with Spring Scheduled or Schedules or Async";
					}

					try {
						Class<?> declaringClass = Class.forName(metadata.getDeclaringClassName(), false, classLoader);
						if (AnnotatedElementUtils.findMergedAnnotation(declaringClass, Async.class) != null) {
							return "the type " + metadata.getDeclaringClassName() + " is annotated "
									+ DistributedLock.class.getName() + " with Spring Async";
						}
					} catch (ClassNotFoundException e) {
						throw new RuntimeException("Class forName error", e);
					}
				}

				try {
					Class<?> declaringClass = Class.forName(metadata.getDeclaringClassName(), false, classLoader);
					Method[] methodArray = declaringClass.getDeclaredMethods();
					final List<Class<?>> superclassList = ClassUtils.getAllSuperclasses(declaringClass);
					for (final Class<?> klass : superclassList) {
						methodArray = ArrayUtils.addAll(methodArray, klass.getDeclaredMethods());
					}
					for (final Method method : methodArray) {
						if (method.getName().equals(metadata.getMethodName()) && method.isAnnotationPresent(DistributedLock.class)
								&& !Modifier.isPublic(method.getModifiers())) {
							return "the method " + metadata.getDeclaringClassName() + "." + metadata.getMethodName()
									+ " is annotated " + DistributedLock.class.getName() + " is not public method";
						}
					}
				} catch (ClassNotFoundException e) {
					throw new RuntimeException("Class forName error", e);
				}
			}
			return null;
		};

		return new DesignConstraintsSupport(distributedLockFunc);
	}

	@FunctionalInterface
	public static interface DesignConstraintsFunction {
		String check(MetadataReader metadataReader, ClassLoader classLoader) throws Exception;
	}

	public static class DesignConstraintsSupport implements CommandLineRunner {
		private static final Logger LOG = LoggerFactory.getLogger(DesignConstraintsSupport.class);
		private DesignConstraintsFunction[] functions;
		private PathMatchingResourcePatternResolver patternResolver;
		private CachingMetadataReaderFactory metadataReaderFactory;

		public DesignConstraintsSupport(DesignConstraintsFunction... functions) {
			this.functions = functions;
			ResourceLoader resourceLoader = new DefaultResourceLoader();
			this.patternResolver = new PathMatchingResourcePatternResolver(resourceLoader);
			this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
			this.metadataReaderFactory.setCacheLimit(1024 * 1024 * 30);
		}

		@Override
		public void run(String... args) throws Exception {
			try {
				LOG.debug("begin");
				this.start();
			} finally {
				functions = null;
				patternResolver = null;
				metadataReaderFactory.clearCache();
				metadataReaderFactory = null;
				LOG.debug("end");
			}
		}

		private void start() {
			if (functions == null) {
				return;
			}
			Package[] packages = Package.getPackages();
			if (packages == null) {
				return;
			}
			for (Package packagee : packages) {
				String basePackage = packagee.getName();
				String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
						+ org.springframework.util.ClassUtils.convertClassNameToResourcePath(basePackage) + "/**/*.class";
				try {
					Resource[] resources = patternResolver.getResources(packageSearchPath);
					if (resources == null) {
						continue;
					}
					for (Resource resource : resources) {
						if (resource.isReadable()) {
							MetadataReader metadataReader = null;
							try {
								metadataReader = metadataReaderFactory.getMetadataReader(resource);
							} catch (Throwable t) {
								//NoClassDefFoundError
								LOG.debug("parse resource {} metadata error", resource.getURL().toString(), t);
							}
							if (metadataReader == null) {
								continue;
							}
							for (DesignConstraintsFunction function : functions) {
								String checkedMsg = null;
								try {
									checkedMsg = function.check(metadataReader, patternResolver.getClassLoader());
								} catch (Throwable t) {
									checkedMsg = t.getMessage() == null ? "check error"
											: t.getMessage();
									LOG.error(t.getMessage(), t);
								}
								if (checkedMsg != null) {
									LOG.error("system exit , check do not pass: {}", checkedMsg);
									System.exit(1);
									return;
								}
							}
						}
					}
				} catch (IOException e) {
					throw new RuntimeException("DesignConstraints error", e);
				}
			}
		}

	}
}
