xx平台

Web后端开发框架

技术栈

* java 8
* spring-boot 2.5.12
* swagger 3.0.0
* guava 30.1-jre
* mybait.spring 2.2.2
* mysql 8
* HikariCP 3.4.5
* xxl-job 2.1.0
* lombok 1.18.16
* Jedis 3.2.0
* Elasticsearch 7
* zookeeper client 4.3.0

项目名称：spring-boot-seed

* seed-client：常用的组件client
* seed-common：通用类和util
* seed-task：集成xxl-job做定时任务
    1. [xxl-job](https://github.com/xuxueli/xxl-job)
    2. [shedlock](https://github.com/lukas-krecan/ShedLock)
* seed-web：web后端服务，数据库mysql，用spring mybatis操作数据库
