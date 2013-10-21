#!/bin/sh

cd wills-utils
mvn clean 
mvn install
cd ..


cd wills-model
mvn clean
mvn install
cd ..


cd wills-interface
mvn clean 
mvn install
cd ..

cd wills-rmi-client
mvn clean
mvn install
cd ..

cd wills-service
mvn clean
mvn install
cd ..

cd wills-rmi-server
mvn clean 
mvn package


#pid=`ps aux|grep tomcat|grep -v grep|awk '{print $2}'`
#kill -9 $pid


echo ============remove old root========
rm -rf /Users/renren/svn/wills-mobileapp-java/wills-rmi-server/target/ROOT

echo =========rename new root=================
mv /Users/renren/svn/wills-mobileapp-java/wills-rmi-server/target/wills-rmi-server-1.0.0-SNAPSHOT  /Users/renren/svn/wills-mobileapp-java/wills-rmi-server/target/ROOT

#/Users/renren/svn/tg_tomcat/bin/startup.sh
