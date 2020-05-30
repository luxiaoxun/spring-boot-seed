package com.luxx.service.config;

import java.util.List;
import java.util.Map;

import com.luxx.model.config.Config;

public interface ConfigService {
    Config selectConfig(String name);
    
    List<Config> selectConfigs(String configPrefix);
    
    Map<String, Object> selectMapConfigs(String configPrefix, boolean removePrefix);
    
    int saveConfig(Config record);
    
    int updateConfig(Config record);
    
    int deleteConfig(String name);
    
    void refresh();
}
