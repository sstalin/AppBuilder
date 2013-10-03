#!/bin/sh

HOME=`dirname $0`/..

LIBPATH=$HOME/lib/ext/groovy-all-2.1.1.jar:$HOME/lib/ext/ant-1.8.2.jar:$HOME/lib/ext/ant-launcher-1.8.2.jar:$HOME/lib/ext/commons-lang3-3.1.jar:$HOME/lib/ext/jyaml-1.3.jar

CLASSPATH=$HOME/classes:$HOME/work:$LIBPATH:$HOME/../Translator/classes

CLASSPATH1=$HOME/classes:$LIBPATH:$HOME/lib/ext/htmlcleaner-2.2.jar:$HOME/../Translator/classes

CLASSPATH2==$HOME/work:$HOME/lib/appbuilder.jar:$HOME/lib/translator.jar:$LIBPATH 