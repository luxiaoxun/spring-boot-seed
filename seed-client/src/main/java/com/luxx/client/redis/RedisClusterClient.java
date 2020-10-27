package com.luxx.client.redis;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

@Lazy
@Component
public class RedisClusterClient implements InitializingBean {

    @Value("${redis.address}")
    private String redisNodes;

    private JedisCluster jedisCluster;

    public JedisCluster getJedisCluster() {
        return jedisCluster;
    }

    private static final Logger logger = LoggerFactory.getLogger(RedisClusterClient.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        Set<HostAndPort> hostAndPorts = new HashSet<HostAndPort>();
        logger.info("Redis cluster nodes: " + redisNodes);
        String[] nodeArray = redisNodes.split(",");
        for (String node : nodeArray) {
            String[] host = node.split(":");
            hostAndPorts.add(new HostAndPort(host[0], Integer.parseInt(host[1])));
        }
        JedisPoolConfig jpc = new JedisPoolConfig();
        jpc.setMaxTotal(16);
        jpc.setMaxIdle(16);
        jpc.setMaxWaitMillis(1000L);
        int timeout = 3000;
        int maxAttempts = 5;
        jedisCluster = new JedisCluster(hostAndPorts, timeout, maxAttempts, jpc);
    }

    @PreDestroy
    public void destroy() throws IOException {
        jedisCluster.close();
    }
}
