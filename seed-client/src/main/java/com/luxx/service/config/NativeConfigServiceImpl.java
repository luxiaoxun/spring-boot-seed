package com.luxx.service.config;

import com.luxx.mapper.ConfigMapper;
import com.luxx.model.config.Config;
import com.luxx.utils.ConfigUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Qualifier("NoCache")
@Service
public class NativeConfigServiceImpl implements ConfigService {
    @Autowired
    private ConfigMapper configMapper;

    @Override
    public Config selectConfig(String name) {
        return configMapper.selectByName(name);
    }

    @Override
    public List<Config> selectConfigs(String configPrefix) {
        return configMapper.selectByNamePrefix(configPrefix);
    }

    @Override
    public Map<String, Object> selectMapConfigs(String configPrefix, boolean removePrefix) {
        List<Config> configs = selectConfigs(configPrefix);

        if (removePrefix) {
            return ConfigUtils.getMap(configs, configPrefix.length());
        } else {
            return ConfigUtils.getMap(configs);
        }
    }

    @Override
    public int saveConfig(Config record) {
        record.setUpdateTime(new Date());
        return configMapper.insert(record);
    }

    @Override
    public int updateConfig(Config record) {
        record.setUpdateTime(new Date());
        return configMapper.update(record);
    }

    @Override
    public int deleteConfig(String name) {
        return configMapper.deleteByName(name);
    }

    @Override
    public void refresh() {
    }
}
