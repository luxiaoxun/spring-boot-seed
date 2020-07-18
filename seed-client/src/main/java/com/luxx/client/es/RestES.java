package com.luxx.client.es;

import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.http.HttpHost;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
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
public class RestES {
    private static final Logger logger = LoggerFactory.getLogger(RestES.class);

    @Value("${zone}")
    private String zone;

    @Value("${sys.es.cluster.name}")
    private String esCluster;

    @Value("${sys.es.http.address}")
    private String esHttpAddress;

    private final Cache<String, Set<String>> filedCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).build();

    private RestHighLevelClient _client;

    private Map<String, RestHighLevelClient> clients = new HashMap<>();

    public RestHighLevelClient getClient() {
        return _client;
    }

    @PostConstruct
    public void init() throws Exception {
        _client = getClient(zone);
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

                    Response response = client.getLowLevelClient().performRequest("GET", endpoint);

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

    private RestHighLevelClient _getClient(String zone) throws NumberFormatException, UnknownHostException {
        logger.info("es.http.address: " + esHttpAddress);

        LinkedList<HttpHost> httpPorts = new LinkedList<>();
        for (String address : esHttpAddress.split(",")) {
            String[] hostPort = address.split(":");
            httpPorts.add(new HttpHost(hostPort[0], Integer.parseInt(hostPort[1])));
        }

        return new RestHighLevelClient(RestClient.builder(httpPorts.toArray(new HttpHost[0])));
    }

    public String getZone() {
        return zone;
    }
}
