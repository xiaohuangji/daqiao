#!/bin/sh

cd wills-mcp

mvn clean
mvn package


pid=`ps aux|grep "/Users/renren/svn/mcp_tomcat/"|grep -v grep|awk '{print $2}'`
kill -9 $pid

rm -rf target/ROOT
mv target/wills-mcp target/ROOT


/Users/renren/svn/mcp_tomcat/bin/startup.sh
