#!/bin/sh
svn up
mvn clean
mvn package -U
svn ps svn:keywords "Id" -R src
svn commit -m "rebuild and deploy"
mvn deploy