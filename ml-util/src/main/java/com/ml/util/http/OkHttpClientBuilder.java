package com.ml.util.http;

import okhttp3.Dispatcher;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.internal.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * okhttp客户端构造器
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月19日 上午9:53:30
 */
public final class OkHttpClientBuilder {
	/** OkHttp async executorService config properties */
	private ThreadPoolConfigProperties threadPoolConfigProperties;

	private DispatcherConfigProperties dispatcherConfigProperties;

	private List<Interceptor> interceptors;

	/** 连接创建超时时长,默认1秒 */
	private Long connectTimeout = 1000L;
	private TimeUnit connectTimeoutUnit = TimeUnit.MILLISECONDS;
	/** 连接读超时时长,默认1秒 */
	private Long readTimeout = 1000L;
	private TimeUnit readTimeoutUnit = TimeUnit.MILLISECONDS;

	OkHttpClientBuilder() {
		threadPoolConfigProperties = new ThreadPoolConfigProperties(this);
		dispatcherConfigProperties = new DispatcherConfigProperties(this);
		interceptors = new ArrayList<>();
	}

	public static class ThreadPoolConfigProperties {
		private OkHttpClientBuilder builder;
		/** demo thread size */
		private Integer coreThreadSize = 0;
		/** max pool size */
		private Integer maxThreadSize = 2000;
		/** thread keep alive time */
		private Long threadKeepAliveTime = 60000L;
		/** thread keep alive time unit */
		private TimeUnit threadKeepAliveTimeUnit = TimeUnit.MILLISECONDS;

		ThreadPoolConfigProperties(OkHttpClientBuilder builder) {
			this.builder = builder;
		}

		public ThreadPoolConfigProperties coreThreadSize(Integer coreThreadSize) {
			if (null != coreThreadSize) {
				this.coreThreadSize = coreThreadSize;
			}
			return this;
		}

		public ThreadPoolConfigProperties maxThreadSize(Integer maxThreadSize) {
			if (null != maxThreadSize) {
				this.maxThreadSize = maxThreadSize;
			}
			return this;
		}

		public ThreadPoolConfigProperties threadKeepAliveTime(Long threadKeepAliveTime) {
			if (null != threadKeepAliveTime) {
				this.threadKeepAliveTime = threadKeepAliveTime;
			}
			return this;
		}

		public ThreadPoolConfigProperties threadKeepAliveTimeUnit(TimeUnit threadKeepAliveTimeUnit) {
			this.threadKeepAliveTimeUnit = threadKeepAliveTimeUnit;
			return this;
		}

		public OkHttpClientBuilder builder() {

			return builder;
		}
	}

	public static class DispatcherConfigProperties {
		private OkHttpClientBuilder builder;
		/** maxRequests */
		private Integer maxRequests = 64;
		/** maxRequestsPerHost */
		private Integer maxRequestsPerHost = 5;

		DispatcherConfigProperties(OkHttpClientBuilder builder) {
			this.builder = builder;
		}

		public DispatcherConfigProperties maxRequests(Integer maxRequests) {
			if (null != maxRequests) {
				this.maxRequests = maxRequests;
			}
			return this;
		}

		public DispatcherConfigProperties maxRequestsPerHost(Integer maxRequestsPerHost) {
			if (null != maxRequestsPerHost) {
				this.maxRequestsPerHost = maxRequestsPerHost;
			}
			return this;
		}

		public OkHttpClientBuilder builder() {

			return builder;
		}
	}

	public static OkHttpClientBuilder getInstance() {
		return new OkHttpClientBuilder();
	}

	public ThreadPoolConfigProperties threadPoolConfigProperties() {
		return threadPoolConfigProperties;
	}

	public DispatcherConfigProperties dispatcherConfigProperties() {
		return dispatcherConfigProperties;
	}

	public List<Interceptor> interceptors() {
		return interceptors;
	}

	public OkHttpClientBuilder connectTimeout(Long connectTimeout) {
		connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
		return this;
	}

	public OkHttpClientBuilder connectTimeout(Long connectTimeout, TimeUnit unit) {
		if (null != connectTimeout) {
			this.connectTimeout = connectTimeout;
		}
		this.connectTimeoutUnit = unit;
		return this;
	}

	public OkHttpClientBuilder readTimeout(Long readTimeout) {
		readTimeout(readTimeout, TimeUnit.MILLISECONDS);
		return this;
	}

	public OkHttpClientBuilder readTimeout(Long readTimeout, TimeUnit unit) {
		if (null != readTimeout) {
			this.readTimeout = readTimeout;
		}
		this.readTimeoutUnit = unit;
		return this;
	}

	public OkHttpClientBuilder interceptors(List<Interceptor> interceptors) {
		this.interceptors = interceptors;

		return this;
	}

	public OkHttpClientBuilder interceptor(Interceptor interceptor) {
		if (null != interceptor) {
			this.interceptors.add(interceptor);
		}

		return this;
	}

	public OkHttpClientBuilder basicAuthenticate(String username, String password) {
		AuthenticationInterceptor authenticationInterceptor = new AuthenticationInterceptor();

		authenticationInterceptor.basicAuthentication(username, password);

		return interceptor(authenticationInterceptor);
	}

	public OkHttpClient build(Interceptor interceptor) {
		return interceptor(interceptor).build();
	}

	public OkHttpClient build() {
		ExecutorService executorService = new ThreadPoolExecutor(threadPoolConfigProperties.coreThreadSize,
				threadPoolConfigProperties.maxThreadSize, threadPoolConfigProperties.threadKeepAliveTime,
				threadPoolConfigProperties.threadKeepAliveTimeUnit, new SynchronousQueue<>(),
				Util.threadFactory("OkHttp Dispatcher", false));

		Dispatcher dispatcher = new Dispatcher(executorService);
		dispatcher.setMaxRequests(dispatcherConfigProperties.maxRequests);
		dispatcher.setMaxRequestsPerHost(dispatcherConfigProperties.maxRequestsPerHost);

		OkHttpClient.Builder builder = new OkHttpClient.Builder();

		for (Interceptor interceptor: interceptors) {
			builder.addInterceptor(interceptor);
		}

		return builder.dispatcher(dispatcher).retryOnConnectionFailure(false)
				.connectTimeout(connectTimeout, connectTimeoutUnit)
				.readTimeout(readTimeout, readTimeoutUnit)
				.build();
	}
}
