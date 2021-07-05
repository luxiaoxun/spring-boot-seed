package com.luxx.client.es;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@Lazy
@Component
public class ES {
    private static final Logger logger = LoggerFactory.getLogger(ES.class);

    @Value("${zone}")
    private String zone;

    @Value("${es.address}")
    private String esAddress;

    @Value("${es.username}")
    private String esUsername;

    @Value("${es.password}")
    private String esPassword;

    private final Cache<String, Set<String>> filedCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES).build();

    private RestHighLevelClient _client;

    private Map<String, RestHighLevelClient> clients = new HashMap<>();

    public RestHighLevelClient getClient() {
        return _client;
    }

    @PostConstruct
    public void init() {
        _client = getClient(zone);
    }

    public void cleanFieldCache() {
        filedCache.invalidateAll();
    }

    @PreDestroy
    public void destroy() {
        clients.values().forEach(client -> {
            try {
                client.close();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        });
    }

    public synchronized RestHighLevelClient getClient(String zone) {
        RestHighLevelClient client = clients.get(zone);
        if (client == null) {
            try {
                client = _getClient(zone);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            clients.put(zone, client);
        }

        return client;
    }

    private RestHighLevelClient _getClient(String zone) throws NumberFormatException {
        logger.info("es.cluster.address: " + esAddress);
        String[] hostPort = esAddress.split(":");
        RestClientBuilder builder = RestClient.builder(new HttpHost(hostPort[0], Integer.parseInt(hostPort[1])))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                        credentialsProvider.setCredentials(AuthScope.ANY,
                                new UsernamePasswordCredentials(esUsername, esPassword));
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }
                }).setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
                    @Override
                    public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder builder) {
                        builder.setConnectTimeout(10000)
                                .setSocketTimeout(60000)
                                .setConnectionRequestTimeout(0);
                        return builder;
                    }
                });

        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
    }

    public String getZone() {
        return zone;
    }
}
