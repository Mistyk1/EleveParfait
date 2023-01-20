#!/bin/bash
SCRIPTDIR=$(dirname $0)
cd $SCRIPTDIR
CLASSPATH=$(find lib -name "*.jar" | tr '\n' ':')
cd classes
java -cp ../$CLASSPATH:. EleveParfait
cd ..
