# PatPatBackend

## 介绍

---
SCUT三七互娱开发实训patpat
patpat是一个类似于taptap的游戏社区，管理游戏社区后端部分代码

## How to Build

---
build with maven
```shell
cd folderRoot
mvn install
mvn spring-boot:run
```
### Interface url

---
***For the noble client and front-end***
`http://localhost:8080/api-docs`

## Change Log

---
2022-06-15
- Complete the database design

2022-06-16
- Integrate logging and token login authentication

2022-06-17
- Defines the unified return interface
- Completed the login function
- Improve the swagger-ui

2022-06-18
- Merge the code and optimize picture uploading function
- rename `user_info` to `user`
- Completed the registration function and user interface

2022-06-19
- Implemented and tested some user interfaces
- update the settings of image upload

2022-06-20
- Completed the user follow interface

2022-06-22
- Completed the forum interface and game interface

2022-06-23
- Completed the reply interface and post interface

2022-06-24
- Completed the upgrade function

2022-06-26
- The index for the database is established
- Optimized some SQL statements 

2022-06-27
- Configure and write ElasticSearch

2022-06-28
- Integrated the Elasticsearch 
- Test the query function

2022-06-29
- The back-end code is deployed on the CVM
- Deploy mysql and elasticSearch on docker

2022-06-30
- Try to integrate alibaba canal's database synchronization scheme
- Fixed some bugs

2022-07-01
- Released the beta version with code synchronization scheme.