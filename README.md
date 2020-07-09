## Introduction
由于项目中存在接口调用次数统计的功能，所以想做一个spring boot starter的jar包，通过注解的方式，无侵入式统计接口的调用次数。  
基于Spring Boot 2.2.5.RELEASE 开发。
存储的方式有两种：
- MySQL(默认方式)
- MongoDB  


## Project Basic Facts
MySQL的持久化方式用的Spring Data JPA，MongoDB的持久化方式用的MongoTemplate。  
使用了ScheduledThreadPoolExecutor固定周期的刷新List(采用异步添加的方式，先把记录数添加到list)，每隔一段时间刷新到MySQL或者MongoDB。  
目前常用的持久层框架，主要是Spring Data JPA，MyBatis等，以MyBatis为主。为了减少对主项目的jar包依赖的影响，首次开发基于Spring Data JPA，  
已经满足了项目的正常需求。为了使用该Stater的适用性更广泛，减少对持久层框架的影响，基于JdbcTemplate，又开发了一套新的starter。  
call-statistics:
- call-statistics-jdbc
- call-statistics-jpa  

以上两个子工程均支持MongoDB，区别仅在于MySQL持久化。  
如果业务代码使用的持久层框架是Spring Data JPA或者MyBatis，请选择call-statistics-jdbc；  
如果业务代码使用的持久层框架是JdbcTemplate，请选择call-statistics-jpa。


## Quick Start

1.首先使用 mvn clean install -Dmaven.test.skip=true 将本工程安装到本地maven仓库

2.在你的web项目中，引入pom依赖，选择其一
```
<dependency>
    <groupId>org.kaws</groupId>
    <artifactId>call-statistics-jdbc</artifactId>
    <version>1.0.0</version>
</dependency>

<dependency>
    <groupId>org.kaws</groupId>
    <artifactId>call-statistics-jpa</artifactId>
    <version>1.0.0</version>
</dependency>

```

3.基于MySQL的配置方式
```
# 开启mysql的配置，默认为false
call.statistics.mysql.active=true
call.statistics.mysql.driver-class-name=com.mysql.cj.jdbc.Driver
call.statistics.mysql.url=jdbc:mysql://localhost:3306/call?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=false
call.statistics.mysql.username=root
call.statistics.mysql.password=123456
call.statistics.mysql.connection-timeout=30000
call.statistics.mysql.minimum-idle=5
call.statistics.mysql.maximum-pool-size=20
call.statistics.mysql.auto-commit=true
call.statistics.mysql.idle-timeout=600000
call.statistics.mysql.pool-name=DataSourceHikariCP
call.statistics.mysql.max-lifetime=1800000
call.statistics.mysql.connection-test-query=SELECT 1
```
在配置的时候出现了一系列问题，主要是多数据源的问题，笔者演示MyBatis和JdbcTempalte的配置方式。  
数据库连接池以HikariCP为例  
3.1 JdbcTemplate的配置方式
```
@Configuration
public class DataSourceConf {

        /**
         * 因为统计包中已经有数据源了所以需要配置@Primary
         *
         * @return
         */
        @Primary
        @Bean
        /**
         * springboot的多数据源自动装配的时候配置文件中的url需要改成url-jdbc
         * 如果不修改进行数据库访问时会报错：jdbcUrl is required with driverClassName.
         */
        @ConfigurationProperties(prefix = "spring.datasource")
        @Qualifier("mainDataSource")
        public DataSource mainDataSource() {
            return DataSourceBuilder.create().build();
        }

        /**
         * 将上面的数据源注入到mainJdbcTemplate中
         *
         * @param dataSource
         * @return
         */
        @Bean(name = "mainJdbcTemplate")
        public JdbcTemplate jdbcTemplate(@Qualifier("mainDataSource") DataSource dataSource, JdbcProperties properties) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            JdbcProperties.Template template = properties.getTemplate();
            jdbcTemplate.setFetchSize(template.getFetchSize());
            jdbcTemplate.setMaxRows(template.getMaxRows());
            if (template.getQueryTimeout() != null) {
                jdbcTemplate.setQueryTimeout((int)template.getQueryTimeout().getSeconds());
            }
            return jdbcTemplate;
        }
}
```
3.2 MyBatis的配置方式，只需要配置主数据源即可， mybatis-spring-boot-starter会自动加载SqlSessionFactoryBean和MapperScannerConfigurer
```
@Configuration
public class DataSourceConf {

        /**
         * 因为统计包中已经有数据源了所以需要配置@Primary
         *
         * @return mainDataSource
         */
        @Primary
        @Bean
        /**
         * springboot的多数据源自动装配的时候配置文件中的url需要改成url-jdbc
         * 如果不修改进行数据库访问时会报错：jdbcUrl is required with driverClassName.
         */
        @ConfigurationProperties(prefix = "spring.datasource")
        @Qualifier("mainDataSource")
        public DataSource mainDataSource() {
            return DataSourceBuilder.create().build();
        }
}
```

4.基于MongoDB的配置方式
```
call.statistics.mongo.active=true
call.statistics.mongo.uri=mongodb://username:password@localhost:27017/database
# 以下条件为非必填项，存在默认值
call.statistics.mongo.minPoolSize=0

call.statistics.mongo.maxPoolSize=100

# The connection timeout in milliseconds
call.statistics.mongo.connectTimeOut=1000

# The maximum idle time of a pooled connection
call.statistics.mongo.maxIdleTime=0

# he maximum life time of a pooled connection
call.statistics.mongo.maxLifeTime=0

# The socket timeout in milliseconds
call.statistics.mongo.socketTimeout=0

# This multiplier, multiplied with the connectionsPerHost setting, gives the maximum number of threads that may be waiting for a connection to become available from the pool
call.statistics.mongo.waitQueueMultiple=5

# The maximum wait time in milliseconds that a thread may wait for a connection to become available
call.statistics.mongo.waitQueueTimeout=120000

```
没有开启认证的话，可以去除username和password 

5.异步刷库
为了减少对主业务的影响，调用统计记录的入库改为异步批量入库，默认10s刷新一次。  
在AOP的环绕消息里面，先把调用记录存到list，然后通过ScheduleThreadPoolExecutor固定频率获取list里面的数据，刷新到配置好的数据源。
```
# 可以设置ScheduleThreadPoolExecutor的核心线程数量，默认服务器的核心线程数
call.statistics.scheduled.corePoolSize=

```

6.开启统计功能
使用MySQL的方式，需要初始化数据库，请看call.sql
首先在启动类上排除mongo的自动配置
```
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
```
其次在controller的方法上开启注解@CallStatistics和@CallStatistics(value = StorageType.MONGO)，如下：
```
  //@CallStatistics(StorageType.MONGO)
    @CallStatistics
    @GetMapping("/one")
    public RestResponse getEnterpriseInfoById(int id) {
        EnterpriseInfo enterpriseInfo = enterpriseInfoService.getById(id);
        return RestResponse.success(enterpriseInfo);
    }
```
7.自定义统计字段
请求头中里面加入了自定义appID字段，如果想要统计不同的字段可以自行添加，源码(CallAspect)如下：
```
try {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String url = request.getRequestURI();
        // 可以在这里添加用到的字段，并修改相应的持久化方式
        String appid = request.getHeader("appID");
        String method = request.getMethod();
        String params = null;
        if ("GET".equals(method)) {
            Map<String, String[]> parameterMap = request.getParameterMap();
            params = objectMapper.writeValueAsString(parameterMap);
        } else {
            int len = request.getContentLength();
            ServletInputStream inputStream = request.getInputStream();
            byte[] buffer = new byte[len];
            inputStream.read(buffer, 0, len);
            params = new String(buffer);
       }
        url = method + " " + url;
        // 获取调用的ip
        String ipAddr = IPUtil.getIpAddr(request);
    }
```
如果觉得使用过程的字段不能满足要求，可以自定义想用的统计字段。请读者自行阅读源码进行拓展。
