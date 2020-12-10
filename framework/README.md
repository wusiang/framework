# framework
framework工具包

#私服制作教程


##setp 1
###在服务器搭建nexus私服 设置上传参考
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


#注意点
1、首先idea的maven路径不要使用自生maven 使用我们下载的
2、setting.xml文件的server的id要和distributionManagement的repository的id保持一致
3、如果是多模块工程distributionManagement要卸载父pom里面
4、maven的setting.xml文件要摄像拉取jar包的仓库


###maven的setting.xml文件

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
   | This is a list of authentication profiles, keyed by the server-id used within the system.
   | Authentication profiles can be used whenever maven must make a connection to a remote server.
   |-->
  <servers>
      <server>
        <id>sonatype-nexus-snapshots</id>
        <username>admin</username>
        <password>admin123</password>
      </server>
  </servers>

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
			<!--all requests to nexus via the mirror -->
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


