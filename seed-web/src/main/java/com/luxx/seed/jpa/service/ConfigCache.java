package com.luxx.seed.jpa.service;

import com.luxx.seed.jpa.entity.ConfigEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConfigCache {
    @Autowired
    private ConfigCache configCache;

    @Autowired
    private ConfigService configService;

    private final Logger logger = LoggerFactory.getLogger(ConfigCache.class);

    @Cacheable(value = "configCache")
    public Map<String, ConfigEntity> getALLConfigs() {
        Map<String, ConfigEntity> configMap = new HashMap<>();
        List<ConfigEntity> configEntityList = configService.findAll();
        for (ConfigEntity entity : configEntityList) {
            configMap.put(entity.getName(), entity);
        }
        return configMap;
    }

    @Caching(evict = {@CacheEvict(value = "configCache", allEntries = true)})
    public void refresh() {
        logger.info("Config cache refresh.");
    }

    @Scheduled(fixedRate = 60000)
    public void doRefresh() {
        configCache.refresh();
    }
}

