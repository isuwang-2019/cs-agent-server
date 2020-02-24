#!/usr/bin/env bash
pwd=`pwd`
buildPackage="/agent_server-service/target/scala-2.12/agent_server_service-assembly-2.0-SNAPSHOT.jar"
echo build on ${pwd}
sbt clean service/assembly
cp ${pwd}${buildPackage} docker/agent_server.jar
cd docker
docker build -t dapengsoa/agent-server:2.0.0 .
cd ${pwd}
