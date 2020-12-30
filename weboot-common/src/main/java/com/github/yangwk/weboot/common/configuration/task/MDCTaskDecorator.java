package com.github.yangwk.weboot.common.configuration.task;

import java.util.Map;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.yangwk.weboot.common.utils.JsonUtils;
import com.github.yangwk.weboot.common.utils.MDCUtils;

public class MDCTaskDecorator implements TaskDecorator {

	private final String traceIdName;

	public MDCTaskDecorator(String traceIdName) {
		this.traceIdName = traceIdName;
	}

	@Override
	public Runnable decorate(Runnable runnable) {
		return new MDCRunnable(runnable);
	}

	public <V> RunnableScheduledFuture<V> decorate(RunnableScheduledFuture<V> runnableScheduled) {
		return new DelegatedMDCRunnableScheduledFuture<>(runnableScheduled);
	}

	private class MDCRunnable implements Runnable {

		private Runnable originalRunnable;
		private Map<String, String> contextMap;

		private MDCRunnable(Runnable originalRunnable) {
			this.originalRunnable = originalRunnable;
			this.contextMap = JsonUtils.jsonToObject(JsonUtils.objectToJson(MDC.getCopyOfContextMap()), new TypeReference<Map<String, String>>() {
			});
		}

		@Override
		public void run() {
			try {
				if (this.contextMap != null) {
					MDC.setContextMap(this.contextMap);
				}
				MDCUtils.generateTraceIdIfNeed(MDCTaskDecorator.this.traceIdName);

				this.originalRunnable.run();
			} finally {
				MDC.clear();
			}
		}

	}

	private class DelegatedMDCRunnableScheduledFuture<V> extends MDCRunnable implements RunnableScheduledFuture<V> {

		private RunnableScheduledFuture<V> rsf;

		private DelegatedMDCRunnableScheduledFuture(RunnableScheduledFuture<V> rsf) {
			super(rsf);
			this.rsf = rsf;
		}

		@Override
		public boolean cancel(boolean mayInterruptIfRunning) {
			return this.rsf.cancel(mayInterruptIfRunning);
		}

		@Override
		public boolean isCancelled() {
			return this.rsf.isCancelled();
		}

		@Override
		public boolean isDone() {
			return this.rsf.isDone();
		}

		@Override
		public V get() throws InterruptedException, ExecutionException {
			return this.rsf.get();
		}

		@Override
		public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
			return this.rsf.get(timeout, unit);
		}

		@Override
		public long getDelay(TimeUnit unit) {
			return this.rsf.getDelay(unit);
		}

		@Override
		public int compareTo(Delayed o) {
			return this.rsf.compareTo(o);
		}

		@Override
		public boolean isPeriodic() {
			return this.rsf.isPeriodic();
		}

	}

}
