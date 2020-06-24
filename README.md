## Introduction
由于项目中存在接口调用次数统计的功能，所以想做一个spring boot starter的jar包，通过注解的方式，无侵入式统计接口的调用次数。  
基于Spring Boot 2.2.5.RELEASE 开发的。  
存储的方式有两种：
- MySQL(默认方式)
- MongoDB  

MySQL的持久化方式用的Spring Data JPA，MongoDB的持久化方式用的MongoTemplate 


## Quick Start

1.首先使用 mvn clean install -Dmaven.test.skip=true 将本工程安装到本地maven仓库

2.在你的web项目中，引入pom依赖
```
<dependency>
    <groupId>org.kaws</groupId>
    <artifactId>call-statistics-boot-starter</artifactId>
    <version>0.0.1</version>
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
在配置的时候出现了一系列问题，主要是多数据源的问题，笔者演示的的JDBCTempalte的注入方式，MyBatis的方式类似，往SqlSessionFactory注入
相应的数据源即可，必须设置@Primary数据库（如果用到了数据库）。
```
@Configuration
public class DataSourceConf {
       /**
         * 因为统计包中已经有数据源了所以需要配置@Primary
         * 如果不配置@Primary启动就会报错：orm.jpa.HibernateProperties' that could not be found.
         *
         * @return
         */
        @Primary
        @Bean
        /**
         * springboot的多数据源自动装配的时候配置文件中的url需要改成url-jdbc，参考application-dev.yml
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
4.基于MongoDB的配置方式
```
call.statistics.mongo.active=true
call.statistics.mongo.uri= mongodb://username:password@localhost:27017/database

```
没有开启认证的话，可以去除username和password  

5.开启统计功能
根据选取的储存方式的不同，选择不同的注解  
@CallStatistics(value = StorageType.MYSQL)和@CallStatistics(value = StorageType.MONGO)，一般在controller的方法上面。  
使用MySQL的方式，需要初始化数据库，call.sql工程中已经提供。 

6.自定义统计字段
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
```
如果觉得使用过程的字段不能满足要求，可以自定义想用的统计字段。请读者自行阅读源码进行修改。
