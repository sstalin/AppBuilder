#!/bin/sh

source $(dirname $0)/setenv.sh

java -Xshare:off -Xms512m -Xmx2048m -classpath $CLASSPATH xj.mobile.Main $* 

exit 0