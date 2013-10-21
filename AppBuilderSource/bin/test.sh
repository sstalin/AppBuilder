#!/bin/sh

source $(dirname $0)/setenv.sh

java -classpath $CLASSPATH xj.mobile.test.Test0 $* 

exit 0