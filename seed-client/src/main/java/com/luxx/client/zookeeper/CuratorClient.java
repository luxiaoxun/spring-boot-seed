package com.luxx.client.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Lazy
@Component
public class CuratorClient implements InitializingBean {
    @Value("${zookeeper.namespace}")
    private String zkNamespace;

    @Value("${zookeeper.address}")
    private String zkServers;

    private CuratorFramework client;

    @Override
    public void afterPropertiesSet() throws Exception {
        String namespace = zkNamespace;
        String connectString = zkServers;

        client = CuratorFrameworkFactory.builder().namespace(namespace).connectString(connectString)
                .retryPolicy(new ExponentialBackoffRetry(1000, 600)).build();
        client.start();
    }

    public CuratorFramework getClient() {
        return client;
    }

    @PreDestroy
    public void close() {
        client.close();
    }
}
