package com.luxx.service.es;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.mapping.get.GetFieldMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetFieldMappingsResponse;
import org.elasticsearch.action.admin.indices.mapping.get.GetFieldMappingsResponse.FieldMappingMetaData;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import com.luxx.model.config.Config;
import com.luxx.service.config.ConfigService;

@Lazy
@Component
public class ES {
    @Autowired
    private ConfigService configService;
    
    @Value("${zone:}")
    private String zone;
    
    private final Cache<String, Set<String>> filedCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).build();
    
    private Client _client;
    
    private Map<String, Client> clients = new HashMap<>();
    
    private static final Logger logger= LoggerFactory.getLogger(ES.class);
    
    public Client getClient() {
        return _client;
    }

    @PostConstruct
    public void init() throws Exception {
        _client = getClient(zone);
    }
    
    public Set<String> getAllFields(String index) throws Throwable {
        try {
            return filedCache.get(index, new Callable<Set<String>>() {
                @Override
                public Set<String> call() throws Exception {
                    Client client = getClient();
                    
                    GetFieldMappingsResponse mappingsResponse = client.admin().indices().getFieldMappings(new GetFieldMappingsRequest().indices(index).fields("*")).actionGet();
                    
                    Map<String, Map<String, Map<String, FieldMappingMetaData>>> mappings = mappingsResponse.mappings();
                    
                    Set<String> fields = new TreeSet<>();
                    
                    mappings.values().forEach(a -> a.values().forEach(b -> b.values().forEach(c -> {
                        String name = c.fullName();
                        if (!name.startsWith("_")) {
                            fields.add(name);
                        }
                    })));
                    
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
    
    public synchronized Client getClient(String zone) {
        Client client = clients.get(zone);
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
    
    private Client _getClient(String zone) throws NumberFormatException, UnknownHostException {
        Config clusterNameConifg;
        Config addressConifg;
        if (StringUtils.isBlank(zone)) {
            clusterNameConifg = configService.selectConfig("sys.es.cluster.name");
            addressConifg = configService.selectConfig("sys.es.address");
        } else {
            clusterNameConifg = configService.selectConfig("sys." + zone + ".es.cluster.name");
            addressConifg = configService.selectConfig("sys." + zone + ".es.address");
        }
        
        Settings settings = Settings.builder()
                .put("cluster.name", clusterNameConifg.getValue())
                .put("client.transport.sniff", false).build();
        
        Client client = new PreBuiltTransportClient(settings);
        
        for (String address : addressConifg.getValue().split(",")) {
            String[] hostPort = address.split(":");
            ((TransportClient) client).addTransportAddress(new TransportAddress(
                    InetAddress.getByName(hostPort[0]),
                    Integer.valueOf(hostPort[1])));
        }
        
        return client;
    }
    
    public String getZone() {
        return zone;
    }
}
