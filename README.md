# 项目文档
尚医通项目，完结撒花，断断续续地写这个项目，花费了很多时间，中间也踩了许多坑，幸而坚持下来了。

## 后端
这个项目除了使用原有教程提到的技术外，另外添加了一个加密工具`jasypt-spring-boot-starter`，专门用于加密配置文件中的敏感内容，比如阿里云的密钥，数据库密码。跑代码的时候一定把service模块中application.properties中相关的内容删除掉，如下：
```java
jasypt.encryptor.iv-generator-classname=org.jasypt.iv.NoIvGenerator
jasypt.encryptor.algorithm=PBEWithMD5AndDES
```
这两行在配置文件中的开头。

## 前端
这个项目包含了完整的前端代码，包括后台管理系统和前台用户用户系统，可点击下面的链接跳转

[后台管理系统](https://github.com/dancvv/hospital_front_end)

[前台用户系统](https://github.com/dancvv/hospital_front_nuxt)

## 更新内容
添加了数据库sql表
