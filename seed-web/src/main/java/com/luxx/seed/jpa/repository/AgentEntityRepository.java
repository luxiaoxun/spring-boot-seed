package com.luxx.seed.jpa.repository;

import com.luxx.seed.jpa.entity.AgentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentEntityRepository extends JpaRepository<AgentEntity, Long> {
    AgentEntity findByIp(String ip);
}
