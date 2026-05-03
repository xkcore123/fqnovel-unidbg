#!/bin/bash
# 指定 Java 路径
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
export PATH=$JAVA_HOME/bin:$PATH

# 使用项目自带的 mvnw 编译并运行
./mvnw clean package -DskipTests
java -jar target/unidbg-boot-server-0.0.1-SNAPSHOT.jar
