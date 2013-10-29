#!/bin/sh

cd wills-admin-server
mvn clean package
cd target

rm -rf ROOT

mv wills-admin-server-1.0.0-SNAPSHOT ROOT

#pid=`ps aux|grep admin_tomcat|grep -v grep | awk '{print $2}'`

#kill -9 $pid

#/Users/renren/svn/admin_tomcat/bin/startup.sh
