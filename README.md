# spring-boot-web-base
springboot web项目脚手架

## 简介
springboot web项目脚手架主要是将web api项目的基础功能进行拆包整理，让开发人员在开发过程中聚焦于业务开发。

## 如何进行本地测试

1、对ywrain-parent-app进行打包
mvn clean install

2、对ywrain-parent-lib进行打包
mvn clean install

3、执行ywrain-service-case项目的App.class文件

4、可以根据com.ywrain.controller.CaseController的示例进行调试，了解脚手架做了什么
