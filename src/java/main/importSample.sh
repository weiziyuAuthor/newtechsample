#! /usr/bin/env bash

JAVA=$JAVA_HOME/bin/java

CLASSPATH=${CLASSPATH}:$JAVA_HOME/lib/tools.jar

dir=`pwd`
for f in ${dir}/lib/*.jar; do
  CLASSPATH=${CLASSPATH}:$f;
done

date;
$JAVA_HOME/bin/java -Xms5G -Xmx5G -XX:PermSize=256m -XX:MaxPermSize=256m -XX:+UseParallelOldGC  -cp $CLASSPATH:${dir}/Sample-0.0.1-SNAPSHOT.jar com.yu.sample.zookeeper.OriginalClientTest
date;

exit 0