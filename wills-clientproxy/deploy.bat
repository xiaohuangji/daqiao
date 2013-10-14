set MAVEN_OPTS=-Dfile.encoding=utf-8
cd /d %~dp0
svn up
call mvn clean
call mvn package -U
call mvn deploy
pause