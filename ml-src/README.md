项目：src集群版
域名：http://src.ml.com/src
##===============================================
######## node-0 ##########
jar命令(在src目录下运行):mvn clean  -f pom.xml  install -am -DskipTests -pl src-node0
取src-node0/target目录下的dist.jar
运行命令:
	dev: java -jar dist.jar --spring.profiles.active=dev
    test: java -jar dist.jar --spring.profiles.active=test
    prod: java -jar dist.jar --spring.profiles.active=prod
路径前缀:/src

######## node-1 ##########
jar命令(在src目录下运行):mvn clean  -f pom.xml  install -am -DskipTests -pl src-node1
取src-node1/target目录下的dist.jar
运行命令:
	dev: java -jar dist.jar --spring.profiles.active=dev
    test: java -jar dist.jar --spring.profiles.active=test
    prod: java -jar dist.jar --spring.profiles.active=prod
路径前缀:/src

######## node-2 ##########
jar命令(在src目录下运行):mvn clean  -f pom.xml  install -am -DskipTests -pl src-node2
取src-node2/target目录下的dist.jar
运行命令:
	dev: java -jar dist.jar --spring.profiles.active=dev
    test: java -jar dist.jar --spring.profiles.active=test
    prod: java -jar dist.jar --spring.profiles.active=prod
路径前缀:/src