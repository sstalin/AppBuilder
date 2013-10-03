#!/bin/sh

source $(dirname $0)/setenv.sh
HOME=`dirname $0`/..

java -classpath $CLASSPATH:$HOME/lib/ext/gprof-0.2.0.jar xj.mobile.tool.Profiler $* 

exit 0