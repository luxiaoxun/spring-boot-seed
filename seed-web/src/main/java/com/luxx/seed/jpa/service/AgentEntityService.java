package com.luxx.seed.jpa.service;

import com.luxx.seed.jpa.repository.AgentEntityRepository;
import com.luxx.seed.service.JpaService;
import com.luxx.seed.jpa.entity.AgentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgentEntityService extends JpaService<AgentEntity> {
    @Autowired
    private AgentEntityRepository agentEntityRepository;

    public AgentEntity findByIp(String ip) {
        return agentEntityRepository.findByIp(ip);
    }
}
