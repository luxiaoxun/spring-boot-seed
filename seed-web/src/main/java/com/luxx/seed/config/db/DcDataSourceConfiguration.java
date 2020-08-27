package com.luxx.seed.config.db;

import com.luxx.seed.config.annotation.DcMapper;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.luxx.seed.dao.**", sqlSessionFactoryRef = "dcSqlSessionFactory",
        annotationClass = DcMapper.class)
public class DcDataSourceConfiguration {
    @Bean(name = "dcDataSource")
    @ConfigurationProperties("spring.datasource")
    @Primary
    public DataSource dcDataSource() {
        return new HikariDataSource();
    }

    @Bean(name = "dcSqlSessionFactory")
    @Primary
    public SqlSessionFactory dcSqlSessionFactory(@Qualifier("dcDataSource") DataSource dataSource,
                                                 @Value("${spring.mybatis.mapper-locations}") String configLocationResource)
            throws Exception {
        return MybatisConfiguration
                .buildSqlSessionFactory(dataSource, configLocationResource, null);
    }

}
