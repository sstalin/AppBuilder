#!/bin/sh

source $(dirname $0)/setenv.sh

java -classpath $CLASSPATH1:$HOME/scripts/ xj.mobile.tool.GeneratePropertyClass $* 

exit 0