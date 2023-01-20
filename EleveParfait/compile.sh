#!/bin/bash
SCRIPTDIR=$(dirname $0)
cd $SCRIPTDIR
export SOURCESPATH=$(find src -name "*.java")
export CLASSPATH=$(find lib -name "*.jar" | tr '\n' ' ')

javac -cp $CLASSPATH -sourcepath src -d classes $SOURCESPATH
