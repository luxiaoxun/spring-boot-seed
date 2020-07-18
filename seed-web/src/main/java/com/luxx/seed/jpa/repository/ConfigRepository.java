package com.luxx.seed.jpa.repository;

import com.luxx.seed.jpa.entity.ConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by luxiaoxun on 2020/5/15.
 */
public interface ConfigRepository extends JpaRepository<ConfigEntity, Long> {
    ConfigEntity findByName(String name);

    List<ConfigEntity> findByNameLike(String namePrefix);

    void deleteByName(String name);
}
