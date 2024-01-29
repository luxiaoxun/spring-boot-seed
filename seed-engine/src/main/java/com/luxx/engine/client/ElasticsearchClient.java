package com.luxx.engine.client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.*;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.core.TimeValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Component
@Lazy
public class ElasticsearchClient {
    private static Logger logger = LogManager.getLogger(ElasticsearchClient.class);

    @Value("${es.address}")
    private String esAddress;

    @Value("${es.auth.enabled:false}")
    private boolean authEnabled;

    @Value("${es.auth.username:}")
    private String username;

    @Value("${es.auth.password:}")
    private String password;

    // ES Client
    private RestHighLevelClient client;

    private BulkProcessor bulkProcessor;
    private int bulkSize = 5;
    private int bulkActions = 500;
    private int flushInterval = 3;
    private int concurrentRequests = 10;

    public RestHighLevelClient getClient() {
        return client;
    }

    @PostConstruct
    public void init() throws UnknownHostException {
        logger.info("es.address: " + esAddress);
        String[] hostPort = esAddress.split(":");
        RestClientBuilder builder = RestClient.builder(new HttpHost(hostPort[0], Integer.parseInt(hostPort[1])));
        builder.setRequestConfigCallback(requestConfigBuilder ->
                requestConfigBuilder.setConnectTimeout(5000).setSocketTimeout(60000));
        builder.setHttpClientConfigCallback(httpClientConfig ->
                httpClientConfig.setKeepAliveStrategy((response, context) -> Duration.ofMinutes(5).toMillis()));

        if (authEnabled && StringUtils.hasLength(username) && StringUtils.hasLength(password)) {
            logger.info("es auth enabled");
            builder.setHttpClientConfigCallback(httpClientConfig -> {
                CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
                return httpClientConfig.setDefaultCredentialsProvider(credentialsProvider);
            });
        }

        client = new RestHighLevelClient(builder);

        BulkProcessor.Listener listener = new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest request) {
                logger.debug("ES before bulk, number of actions: {}", request.numberOfActions());
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, BulkResponse responses) {
                if (responses.hasFailures()) {
                    try {
                        StringBuilder sb = new StringBuilder("Failure in bulk execution: ");
                        int pos = responses.getItems().length - 1;
                        BulkItemResponse response = responses.getItems()[pos];
                        while (!response.isFailed() && pos >= 0) {
                            response = responses.getItems()[pos];
                            --pos;
                        }
                        sb.append("\n[").append(pos)
                                .append("]: index [").append(response.getIndex())
                                .append("], type [").append(response.getType())
                                .append("], id [").append(response.getId())
                                .append("], message [").append(response.getFailureMessage())
                                .append("]");
                        logger.warn(sb.toString());
                    } catch (Exception e) {
                        logger.warn("Print es bulk error failed: {}", e.toString());
                        if (logger.isDebugEnabled())
                            logger.debug("ES bulk error: {}", responses.buildFailureMessage());
                    }
                }
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                logger.error("ES bulk got exception: {}", failure.toString());
            }
        };

        bulkProcessor = BulkProcessor.builder(
                        (request, bulkListener) -> client.bulkAsync(request, RequestOptions.DEFAULT, bulkListener),
                        listener, "ES-bulk-processor")
                .setBulkActions(bulkActions)
                .setBulkSize(new ByteSizeValue(bulkSize, ByteSizeUnit.MB))
                .setFlushInterval(TimeValue.timeValueSeconds(flushInterval))
                .setConcurrentRequests(concurrentRequests)
                .build();
    }

    @PreDestroy
    public void close() {
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                logger.error(e.toString());
            }
        }
    }

    public void flushBulk() {
        bulkProcessor.flush();
    }

    public void insert(String index, String id, Map<String, Object> data) {
        try {
            IndexRequest request = new IndexRequest(index).source(data)
                    .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            if (!ObjectUtils.isEmpty(id)) {
                request = request.id(id);
            }
            client.index(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            logger.error("ES insert data error: " + e.toString());
        }
    }

    public void upsert(String index, String id, Map<String, Object> data) {
        if (ObjectUtils.isEmpty(id)) {
            bulkProcessor.add(new IndexRequest(index).source(data));
        } else {
            bulkProcessor.add(new UpdateRequest(index, id).doc(data).retryOnConflict(3).docAsUpsert(true));
        }
    }

    public void upsert(String index, String id, Map<String, Object> data, Set<String> excludes) {
        Map<String, Object> doc = data;
        if (!excludes.isEmpty()) {
            doc = data.entrySet().stream().filter(entry -> !excludes.contains(entry.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (lhs, rhs) -> rhs));
        }
        if (ObjectUtils.isEmpty(id)) {
            bulkProcessor.add(new IndexRequest(index).source(doc));
        } else {
            bulkProcessor.add(new UpdateRequest(index, id).doc(doc).retryOnConflict(3).docAsUpsert(true));
        }
    }
}
