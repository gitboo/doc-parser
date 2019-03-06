package com.wmp.docparser.client;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;


public class RestTemplateBuilder {

	private static final int DEFAULT_CONNECTION_TIME_TO_LIVE = 60;

	private static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 100;

	private static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 50;

	private static final int DEFAULT_CONNECTION_POOL_TIMEOUT_MILLISECONDS = 5 * 1000;

	private static final int DEFAULT_CONNECT_TIMEOUT_MILLISECONDS = 10 * 1000;

	private static final int DEFAULT_READ_TIMEOUT_MILLISECONDS = 30 * 1000;

	private static final long DEFAULT_MAX_KEEP_ALIVE_TIMEOUT_MILLISECONDS = -1;

	private int connectionTimeToLive = DEFAULT_CONNECTION_TIME_TO_LIVE;

	private int maxTotalConnections = DEFAULT_MAX_TOTAL_CONNECTIONS;

	private int maxConnectionsPerRoute = DEFAULT_MAX_CONNECTIONS_PER_ROUTE;

	private int connectionPoolTimeout = DEFAULT_CONNECTION_POOL_TIMEOUT_MILLISECONDS;

	private int connectTimeout = DEFAULT_CONNECT_TIMEOUT_MILLISECONDS;

	private int readTimeout = DEFAULT_READ_TIMEOUT_MILLISECONDS;

	private long maxKeepAliveTimeout = DEFAULT_MAX_KEEP_ALIVE_TIMEOUT_MILLISECONDS;

	private boolean tcpNoDelay = true;

	private boolean soKeepAlive = false;

	private boolean keepAlive = true; // HTTP connection keep-alive

	private boolean compressionEnabled = false;


	public RestTemplateBuilder setConnectionTimeToLive(int ttl) {
		this.connectionTimeToLive = ttl;
		return this;
	}


	public RestTemplateBuilder setMaxTotalConnections(int maxTotalConnections) {
		this.maxTotalConnections = maxTotalConnections;
		return this;
	}


	public RestTemplateBuilder setMaxConnectionsPerRoute(int maxConnectionsPerRoute) {
		this.maxConnectionsPerRoute = maxConnectionsPerRoute;
		return this;
	}


	public RestTemplateBuilder setConnectionPoolTimeout(int poolTimeout) {
		this.connectionPoolTimeout = poolTimeout;
		return this;
	}


	public RestTemplateBuilder setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
		return this;
	}


	public RestTemplateBuilder setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
		return this;
	}


	public RestTemplateBuilder setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
		return this;
	}


	public RestTemplateBuilder setMaxKeepAliveTimeout(int keepAliveTimeout) {
		this.maxKeepAliveTimeout = keepAliveTimeout;
		return this;
	}

	public RestTemplateBuilder setCompressionEnabled(boolean enabled) {
		this.compressionEnabled = enabled;
		return this;
	}


	public RestTemplate build() {
		HttpClientBuilder builder = HttpClients.custom()
				.setConnectionTimeToLive(connectionTimeToLive, TimeUnit.MILLISECONDS)
				.setMaxConnTotal(maxTotalConnections)
				.setMaxConnPerRoute(maxConnectionsPerRoute);

		// Socket TCP_NODELAY, SO_KEEPAVLIE, SO_TIMEOUT, SO_LINGER, Send Buffer Size,
		// Receive Buffer Size
		builder.setDefaultSocketConfig(SocketConfig.custom()
				.setTcpNoDelay(tcpNoDelay)
				.setSoKeepAlive(soKeepAlive)
				.build());

		// Request specific configuration i.e. ConnectionPool Timeout
		builder.setDefaultRequestConfig(RequestConfig.custom()
				.setSocketTimeout(readTimeout)
				.setConnectTimeout(connectTimeout)
				.setConnectionRequestTimeout(connectionPoolTimeout)
				.build());

		// HTTP Keep-Alive
		if (keepAlive) {
			builder.setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE);
			builder.setKeepAliveStrategy(new SettableConnectionKeepAliveStrategy(maxKeepAliveTimeout));
		} else {
			builder.setConnectionReuseStrategy(NoConnectionReuseStrategy.INSTANCE);
		}

		// HTTP Content Compression
		if (!compressionEnabled) {
			builder.disableContentCompression();
		}

		CloseableHttpClient client = builder.build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(client);
		return new RestTemplate(new BufferingClientHttpRequestFactory(requestFactory));
	}
}
