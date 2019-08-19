- **swaggerUi 访问**
  http://localhost:9000/swagger-ui/index.html
  或
  http://localhost:8080/swagger-ui/index.html 使用了 thymeleaf

1. 关联关系说明
   Point 中实体关系
   "relationships": [
   {
   "relationshipType": "many-to-one",//代表 points 与外部 User 的关联关系
   "otherEntityName": "user",//代表 points 外部关联的对象 User
   "otherEntityRelationshipName": "points", // 谁要与其他实体关联关系
   "relationshipName": "user",// 当前实体中关联关系的字段
   "relationshipValidateRules": "required", // 关联关系的验证
   "otherEntityField": "login" // 实体与外部 User 关联的字段
   }
   ],

## 创建实体

jhipster import-jdl jhipster-jdl.jh
ynaxdh

1. 命令修改实体
   yo jhipster:entity BloodPressure

2.先生成 changelog 文件
mvnw liquibase:diff

3.将生成的文件添加到 mater.xml 文件中
