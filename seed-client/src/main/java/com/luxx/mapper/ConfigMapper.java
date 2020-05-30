package com.luxx.mapper;

import com.luxx.model.config.Config;

import java.util.List;

/**
 * Created by luxiaoxun on 2020/5/15.
 */
public interface ConfigMapper {

    int insert(Config config);

    int delete(int id);

    int update(Config config);

    Config selectByName(String name);

    List<Config> selectByNamePrefix(String name);

    int deleteByName(String name);

}
