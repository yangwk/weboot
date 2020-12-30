package com.github.yangwk.weboot.common.configuration.task;

import java.util.concurrent.Callable;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.task.TaskSchedulerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.github.yangwk.weboot.common.configprop.MDCConfigProperties;

@Configuration
@EnableConfigurationProperties(MDCConfigProperties.class)
public class TaskSchedulingConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public TaskScheduler taskScheduler(TaskSchedulerBuilder builder, MDCConfigProperties mdcConfigProperties) {
		return builder.configure(new DecoratedThreadPoolTaskScheduler(mdcConfigProperties.getGlobalTraceIdName()));
	}

	private class DecoratedThreadPoolTaskScheduler extends ThreadPoolTaskScheduler {
		private static final long serialVersionUID = 277128627359558428L;

		private final String traceIdName;

		private DecoratedThreadPoolTaskScheduler(String traceIdName) {
			super();
			this.traceIdName = traceIdName;
		}

		@Override
		protected ScheduledExecutorService createExecutor(int poolSize, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
			return new DecoratedScheduledThreadPoolExecutor(poolSize, threadFactory, rejectedExecutionHandler);
		}

		private class DecoratedScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor {

			private MDCTaskDecorator mdcTaskDecorator = new MDCTaskDecorator(DecoratedThreadPoolTaskScheduler.this.traceIdName);

			private DecoratedScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
				super(corePoolSize, threadFactory, handler);
			}

			@Override
			protected <V> RunnableScheduledFuture<V> decorateTask(Runnable runnable, RunnableScheduledFuture<V> task) {
				RunnableScheduledFuture<V> rsf = super.decorateTask(runnable, task);
				return mdcTaskDecorator.decorate(rsf);
			}

			@Override
			protected <V> RunnableScheduledFuture<V> decorateTask(Callable<V> callable, RunnableScheduledFuture<V> task) {
				RunnableScheduledFuture<V> rsf = super.decorateTask(callable, task);
				return mdcTaskDecorator.decorate(rsf);
			}

		}
	}
}
