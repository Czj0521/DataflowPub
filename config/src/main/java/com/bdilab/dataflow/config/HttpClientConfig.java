package com.bdilab.dataflow.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Http client for flink rest.

 * @author wh
 * @date 2021/10/17
 */
@Configuration
public class HttpClientConfig {

    @Value("${flink.http.maxTotal}")
    private Integer maxTotal;

    @Value("${flink.http.defaultMaxPerRoute}")
    private Integer defaultMaxPerRoute;

    @Value("${flink.http.connectTimeout}")
    private Integer connectTimeout;

    @Value("${flink.http.connectionRequestTimeout}")
    private Integer connectionRequestTimeout;

    @Value("${flink.http.socketTimeout}")
    private Integer socketTimeout;

    /**
     * Start by instantiating a connection pool manager
     * and setting the maximum number of connections
     * and the number of concurrent connections.

     */
    @Bean(name = "httpClientConnectionManager")
    public PoolingHttpClientConnectionManager getHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager httpClientConnectionManager =
                new PoolingHttpClientConnectionManager();
        httpClientConnectionManager.setMaxTotal(maxTotal);
        httpClientConnectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        return httpClientConnectionManager;
    }

    /**
     * Instantiate the connection pool and set up the connection pool manager.\

     */
    @Bean(name = "httpClientBuilder")
    public HttpClientBuilder getHttpClientBuilder(
            @Qualifier("httpClientConnectionManager")
                    PoolingHttpClientConnectionManager httpClientConnectionManager
    ) {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setConnectionManager(httpClientConnectionManager);
        return httpClientBuilder;
    }

    /**
     * Inject a connection pool to get the httpClient.

     */
    @Bean(name = "httpClient")
    public CloseableHttpClient getCloseableHttpClient(
            @Qualifier("httpClientBuilder") HttpClientBuilder httpClientBuilder) {
        return httpClientBuilder.build();
    }

    /**
     * Builder is an internal class of RequestConfig.
     * Retrieve a Builder object from RequestConfig's Custom method.
     * Set connection information for builder.
     * Here you can also set proxy, cookieSpec and other properties.
     * You can set it here if you want.

     */
    @Bean(name = "builder")
    public RequestConfig.Builder getBuilder() {
        RequestConfig.Builder builder = RequestConfig.custom();
        return builder.setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(socketTimeout);
    }

    /**
     * Build a RequestConfig object using builder.

     */
    @Bean
    public RequestConfig getRequestConfig(@Qualifier("builder") RequestConfig.Builder builder) {
        return builder.build();
    }
}