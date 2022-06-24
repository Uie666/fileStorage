## 项目介绍

**fileStorage**是一个用于提供文件存储服务的项目，业务场景是用户能够通过网页方便地在线管理本地和远程存储的文件。

## 项目启动

1. 克隆项目
2. 导入`pom.xml`中的maven依赖
3. 使用`fileStorage/src/main/resources/database/fileStorage.sql`脚本生成数据表
4. 修改`fileStorage/src/main/resources/application.yaml`配置文件中数据库配置
5. 启动项目，访问`localhost:8181/fileStorage/index`进行访问，新用户需注册
6. 上传的文件路径在项目根目录下的`fileStorage/classpath/fileContainer`目录下

