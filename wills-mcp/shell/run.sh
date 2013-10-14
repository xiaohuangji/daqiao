#/bin/sh

PROJECT_NAME=renren-mobile-mcs/
RUNNER_HOME=/home/sunji/workspace/

[ $# -lt 1 ] && echo "Usage: run.sh mainclass" && exit 1

######run######
MCS_HOME=${RUNNER_HOME}${PROJECT_NAME}target/${PROJECT_NAME}/
CP=${MCS_HOME}WEB-INF/classes

for P in ${MCS_HOME}WEB-INF/lib/*.jar; do
    CP+=":"${P}
done

#echo $CP

echo "###### params ######"
echo $*
echo "###### start ######"

/home/sunji/Apps/j2sdk/bin/java -cp $CP $* 
