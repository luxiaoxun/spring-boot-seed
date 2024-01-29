xx平台

Web后端开发框架

技术栈

* java 21
* spring-boot 3.2.1
* springdoc-openapi 2.3.0
* guava 32
* mybatis.spring 3.0.3
* mysql 8
* HikariCP 3.4.5
* xxl-job 2.1.0
* lombok 1.18.30
* Elasticsearch 7
* redisson 3.23.2
* sa-token 1.37.0

项目名称：spring-boot-seed

* seed-common：通用Util
* seed-engine：基于kafka和disruptor的数据处理服务
* seed-task：集成xxl-job做定时任务
    1. [xxl-job](https://github.com/xuxueli/xxl-job)
    2. [shedlock](https://github.com/lukas-krecan/ShedLock)
* seed-web：web后端服务，数据库mysql，用spring mybatis操作数据库

* swagger: http://localhost:19090/swagger-ui/index.html
* web ssh demo: http://localhost:19090/websshlogin
