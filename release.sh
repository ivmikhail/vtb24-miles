#!/bin/sh
mvn clean release:prepare release:perform -Darguments=-Dmaven.deploy.skip=true -B