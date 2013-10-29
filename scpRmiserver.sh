#!/bin/sh

scp -r /Users/renren/svn/wills-mobileapp-java/wills-rmi-server/target/ROOT/*  root@10.4.21.52:/data/wills/tg_project/server_source/ROOT

#ssh root@10.4.21.52 '/data/wills/tg_project/server_tomcat/bin/restartRmiServer.sh'



