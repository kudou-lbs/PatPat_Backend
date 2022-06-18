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