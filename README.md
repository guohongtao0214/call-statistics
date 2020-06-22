
## Quick Start

1.首先使用maven clean install -Dmaven.test.skip=true本工程安装到本地maven仓库

2.在你的web项目中，引入pom依赖
<dependency>
    <groupId>org.kaws</groupId>
    <artifactId>call-statistics-boot-starter</artifactId>
    <version>0.0.1</version>
</dependency>

3.在你的web项目中，Controller方法上加入注解：@CallStatistics

4.在你的web项目中，配置统计保存的数据源信息
首先确保目标数据源具备相应的表结构（见call.sql）
然后在web工程中的resource目标下创建dataSource.properties文件
添加内容详见dataSource.properties（自行修改）

5.启动你的web项目进行测试请求，查看数据库里是否添加数据