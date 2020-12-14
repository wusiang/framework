###### 1.拉取镜像

```ssh
docker pull sonatype/nexus3
```

###### 2.启动容器

```ssh
sudo docker run -itd --name nexus3 --restart=always -p 8088:8088 -p 8081:8081 -p 5000:5000 -v /mnt/gv0/nexus-data:/nexus-data sonatype/nexus3
```

###### 3.修改maven settings.xml 这里使用默认用户名 admin 密码 admin123

```xml
<?xml version="1.0" encoding="UTF-8"?>

<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
  <!-- pluginGroups
   | This is a list of additional group identifiers that will be searched when resolving plugins by their prefix, i.e.
   | when invoking a command line like "mvn prefix:goal". Maven will automatically add the group identifiers
   | "org.apache.maven.plugins" and "org.codehaus.mojo" if these are not already contained in the list.
   |-->
  <pluginGroups>

  </pluginGroups>
  <!-- proxies
   | This is a list of proxies which can be used on this machine to connect to the network.
   | Unless otherwise specified (by system property or command-line switch), the first proxy
   | specification in this list marked as active will be used.
   |-->
  <proxies>

  </proxies>

  <!-- servers
   | 这里填写nexus上面的登录账号密码
   |-->
  <servers>
      <server>
        <id>sonatype-nexus-snapshots</id>
        <username>admin</username>
        <password>admin123</password>
      </server>
  </servers>

  <!-- servers
   | 选用阿里云的镜像
   |-->
  <mirrors>
     <mirror>
      <id>nexus-aliyun</id>
      <mirrorOf>central</mirrorOf>
      <name>Nexus aliyun</name>
      <url>http://maven.aliyun.com/nexus/content/groups/public</url>
     </mirror>
  </mirrors>

  <profiles>
    <profile>
      <id>nexus</id>
      <!--Enable snapshots for the built in central repo to direct -->
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <!-- 这是设置仓库 -->
      <repositories>
        <repository>
          <id>sonatype-nexus-snapshots</id>
          <url>http://47.110.94.168:8081/repository/maven-snapshots</url>
          <releases>
            <enabled>false</enabled>
          </releases>
          <snapshots>
            <updatePolicy>always</updatePolicy>
            <enabled>true</enabled>
          </snapshots>
        </repository>
      </repositories>
    </profile>
  </profiles>
</settings>
```



###### 4.如果项目需要发布到nexus,修改pom 添加以下 distributionManagement 内容

```xml
<distributionManagement>
        <repository>
            <id>sonatype-nexus-snapshots</id>
            <name>Releases</name>
            <url>http://47.110.94.168:8081/repository/maven-releases/</url>
        </repository>
        <snapshotRepository>
            <id>sonatype-nexus-snapshots</id>
            <name>Snapshot</name>
            <url>http://47.110.94.168:8081/repository/maven-snapshots/</url>
        </snapshotRepository>
</distributionManagement>
```



###### 5.注意点

```
1、首先idea的maven路径不要使用自生maven 使用我们下载的
2、setting.xml文件的server的id要和distributionManagement的repository的id保持一致
3、如果是多模块工程distributionManagement要卸载父pom里面
4、maven的setting.xml文件要摄像拉取jar包的仓库
```

