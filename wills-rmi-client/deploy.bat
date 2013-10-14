set MAVEN_OPTS=-Dfile.encoding=utf-8
cd /d %~dp0
svn up
call mvn clean
rem call mvn package -U
rem call mvn deploy
call mvn -Dmaven.test.skip=true package -U 
call mvn deploy -Dmaven.test.skip=true
pause