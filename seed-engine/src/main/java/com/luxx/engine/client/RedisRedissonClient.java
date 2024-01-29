package com.luxx.engine.client;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@Lazy
public class RedisRedissonClient {
    private static Logger logger = LogManager.getLogger(RedisRedissonClient.class);

    @Value("${redis.address}")
    private String redisAddress;

    @Value("${redis.auth.enabled:false}")
    private boolean authEnabled;

    @Value("${redis.auth.password:}")
    private String password;

    @Value("${redis.cluster.enable:false}")
    private boolean redisClusterEnable;

    private RedissonClient client;

    public RedissonClient getClient() {
        return client;
    }

    @PostConstruct
    public void init() {
        Config config = new Config();
        if (redisClusterEnable) {
            logger.info("redis cluster address: " + redisAddress);
            ClusterServersConfig serverConfig = config.useClusterServers().addNodeAddress("redis://" + redisAddress);
            if (authEnabled && !ObjectUtils.isEmpty(password)) {
                logger.info("redis password enabled");
                serverConfig.setPassword(password);
            }
        } else {
            logger.info("redis single address: " + redisAddress);
            SingleServerConfig serverConfig = config.useSingleServer().setAddress("redis://" + redisAddress);
            if (authEnabled && !ObjectUtils.isEmpty(password)) {
                logger.info("redis password enabled");
                serverConfig.setPassword(password);
            }
        }
        config.setCodec(new JsonJacksonCodec());
        client = Redisson.create(config);
    }

    @PreDestroy
    public void close() {
        if (client != null) {
            client.shutdown();
        }
    }
}
