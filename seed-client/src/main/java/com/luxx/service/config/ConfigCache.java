package com.luxx.service.config;

import com.luxx.model.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ConfigCache {
    @Autowired
    private ConfigCache configCache;

    private final Logger logger = LoggerFactory.getLogger(ConfigCache.class);

    @Cacheable(value="configCache")
    public Map<String, Config> getALLConfigs() {
       Map<String, Config> configMap = new HashMap<>();
        return configMap;
    }

    @Caching(evict = { @CacheEvict(value = "configCache", allEntries = true) })
    public void refresh() {
        //logger.info("Config cache refresh.");
    }

    @Scheduled(fixedRate=60000)
    public void doRefresh() {
        configCache.refresh();
    }
}

