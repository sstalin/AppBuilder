#!/bin/sh

source $(dirname $0)/setenv.sh

java -classpath $CLASSPATH xj.mobile.tool.SHBrushGroovyM $* 

exit 0