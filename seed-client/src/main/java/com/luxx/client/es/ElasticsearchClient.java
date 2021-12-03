package com.luxx.client.es;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
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
import org.elasticsearch.client.*;
import org.elasticsearch.common.xcontent.XContentHelper;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@Lazy
@Component
public class ElasticsearchClient {
    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchClient.class);

    @Value("${es.address}")
    private String esAddress;

    @Value("${es.username}")
    private String esUsername;

    @Value("${es.password}")
    private String esPassword;

    private final Cache<String, Set<String>> filedCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES).build();

    private RestHighLevelClient client;

    public RestHighLevelClient getClient() {
        return client;
    }

    @PostConstruct
    public void init() {
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
                        builder.setConnectTimeout(5000)
                                .setSocketTimeout(60000)
                                .setConnectionRequestTimeout(0);
                        return builder;
                    }
                });
        client = new RestHighLevelClient(builder);
    }

    @PreDestroy
    public void destroy() {
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                logger.error(e.toString());
            }
        }
    }

    public Set<String> getAllFields(String index) throws Throwable {
        try {
            return filedCache.get(index, new Callable<Set<String>>() {
                @SuppressWarnings("unchecked")
                @Override
                public Set<String> call() throws Exception {
                    Set<String> fields = new TreeSet<>();

                    RestHighLevelClient client = getClient();
                    String endpoint = "/" + index + "/_mapping/_doc/field/*";

                    Request request = new Request("GET", endpoint);
                    Response response = client.getLowLevelClient().performRequest(request);

                    try (InputStream is = response.getEntity().getContent()) {
                        Map<String, Object> map = XContentHelper.convertToMap(XContentType.JSON.xContent(), is, false);

                        for (Object indexInfo : map.values()) {
                            Map<String, Object> indexMap = (Map<String, Object>) indexInfo;
                            Map<String, Object> mappingMap = (Map<String, Object>) indexMap.get("mappings");
                            Map<String, Object> filedMap = (Map<String, Object>) mappingMap.get("_doc");
                            if (filedMap == null) {
                                continue;
                            }

                            for (String key : filedMap.keySet()) {
                                if (!key.startsWith("_")) {
                                    fields.add(key);
                                }
                            }
                        }
                    }

                    return fields;
                }
            });
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }

    public void cleanFieldCache() {
        filedCache.invalidateAll();
    }
}
