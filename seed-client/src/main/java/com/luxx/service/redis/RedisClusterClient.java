package com.luxx.service.redis;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PreDestroy;
import com.luxx.service.config.ConfigService;
import com.luxx.utils.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

@Lazy
@Component
public class RedisClusterClient implements InitializingBean{

    @Autowired
    private ConfigService configService;
    
    private JedisCluster _jedisCluster;
    
    public JedisCluster getJedisCluster() {
        return _jedisCluster;
    }

    private static final Logger logger= LoggerFactory.getLogger(RedisClusterClient.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        Set<HostAndPort> jredisClusterNodes = new HashSet<HostAndPort>();
        String[] nodeArray = ConfigUtils.getStringArrayValue(configService.selectConfig("sys.redis.node"));
        logger.info("Redis Nodes: " + String.join(",", nodeArray));
        for(String node : nodeArray){
            String[] host = node.split(":");
            jredisClusterNodes.add(new HostAndPort(host[0], Integer.valueOf(host[1])));
        }

        JedisPoolConfig jpc = new JedisPoolConfig();
        jpc.setMaxTotal(256); //设置过大，当客户端或服务端出现抖动，可能造成服务端连接过多
        jpc.setMaxIdle(10);
        jpc.setMaxWaitMillis(1000L);
        jpc.setTestOnReturn(false); //Redis Cluster模式下，设置为True，每次操作多进行一次PING，影响性能
        jpc.setTestOnBorrow(false); //同上
        int timeout = 3000;
        int maxAttempts = 20; //设置较大的重试次数，避免操作失败，过大会卡死应用
        _jedisCluster = new JedisCluster(jredisClusterNodes, timeout, maxAttempts, jpc);
    }
    
    @PreDestroy
    public void destroy() throws IOException{
        _jedisCluster.close();
    }
}
