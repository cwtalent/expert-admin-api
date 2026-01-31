# 投票系统后台管理 API

## 技术栈
- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- MySQL 8.0
- Maven

## 数据库配置
1. 创建数据库：
```sql
CREATE DATABASE expert;
```

2. 执行数据库脚本：
```bash
mysql -u root -p expert < database/schema.sql
```

3. 修改 `src/main/resources/application.yml` 中的数据库连接信息（如需要）

## 运行项目
```bash
mvn spring-boot:run
```

或者使用 IDE 直接运行 `ExpertAdminApiApplication.java`

## API 文档
启动项目后，访问：
- Swagger UI: http://localhost:8080/api/swagger-ui.html
- API Docs: http://localhost:8080/api/api-docs

## 主要接口
- `/api/projecttypes` - 项目类型管理
- `/api/projects` - 项目管理
- `/api/experts` - 专家管理
- `/api/loginlogs` - 登录日志
- `/api/votes` - 投票管理
- `/api/statistics` - 统计分析
