## 嵌入maven项目映射数据库表生成实体
1 将项目打包上传到本地仓库,命令 mvn clean install

2 复制resources/generatorConfig.xml至项目resources文件夹下

3 复制pom文件 中插件**添加mybatis generator maven插件**至项目pom文件,

pom文件Reimport后即可在pom文件找到此生成表对应实体插件