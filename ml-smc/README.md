项目：smc
域名：http://smc.ml.com/smc
###############################################
jar命令(在smc目录下运行):mvn clean  -f pom.xml  install -am -DskipTests -pl smc
取smc/target目录下的dist.jar
运行命令:
	dev: java -jar dist.jar --spring.profiles.active=dev
    test: java -jar dist.jar --spring.profiles.active=test
    prod: java -jar dist.jar --spring.profiles.active=prod
路径前缀:/smc