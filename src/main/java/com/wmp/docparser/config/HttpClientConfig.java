package com.wmp.docparser.config;


import com.wmp.docparser.client.RestTemplateBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;


@Configuration
public class HttpClientConfig {

    @Resource
    private Environment env;

    @Bean
    public RestTemplate restTemplate() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

        int maxTotalConnections = env.getRequiredProperty("http.max.total.connections", Integer.class);
        int maxConnectionsPerRoute = env.getRequiredProperty("http.max.connections.per.route", Integer.class);
        int ttl = env.getRequiredProperty("http.ttl", Integer.class);
        boolean keepAlive = env.getRequiredProperty("http.keepalive", Boolean.class);
        int maxKeepAliveTimeout = env.getRequiredProperty("http.keepalive.timeout", Integer.class);
        int connectionTimeout = env.getRequiredProperty("http.connection.timeout", Integer.class);
        int readTimeout = env.getRequiredProperty("http.read.timeout", Integer.class);
        int poolTimeout = env.getRequiredProperty("http.pool.timeout", Integer.class);
        boolean compression = env.getRequiredProperty("http.content.compression", Boolean.class);

        RestTemplate restTemplate = new RestTemplateBuilder().setMaxTotalConnections(maxTotalConnections)
                .setMaxConnectionsPerRoute(maxConnectionsPerRoute)
                .setConnectionTimeToLive(ttl)
                .setKeepAlive(keepAlive)
                .setMaxKeepAliveTimeout(maxKeepAliveTimeout)
                .setConnectTimeout(connectionTimeout)
                .setReadTimeout(readTimeout)
                .setConnectionPoolTimeout(poolTimeout)
                .setCompressionEnabled(compression)
                .build();

        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                .build();
        SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext,
                new NoopHostnameVerifier());
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create()
                .setSSLSocketFactory(connectionFactory)
                .build());
        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }
}
