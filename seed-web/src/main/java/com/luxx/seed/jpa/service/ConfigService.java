package com.luxx.seed.jpa.service;

import com.luxx.seed.jpa.entity.ConfigEntity;
import com.luxx.seed.jpa.repository.ConfigRepository;
import com.luxx.seed.service.JpaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ConfigService extends JpaService<ConfigEntity> {

    @Autowired
    private ConfigRepository configRepository;

    public ConfigEntity selectConfig(String name) {
        return configRepository.findByName(name);
    }

    public List<ConfigEntity> selectConfigs(String configPrefix) {
        return configRepository.findByNameLike(configPrefix);
    }

    public long saveConfig(ConfigEntity record) {
        record.setUpdateTime(new Date());
        ConfigEntity configEntity = configRepository.saveAndFlush(record);
        return configEntity.getId();
    }

    public long updateConfig(ConfigEntity record) {
        record.setUpdateTime(new Date());
        ConfigEntity configEntity = configRepository.saveAndFlush(record);
        return configEntity.getId();
    }

    public void deleteConfig(String name) {
        configRepository.deleteByName(name);
    }

}
