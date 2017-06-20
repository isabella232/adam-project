#!/usr/bin/env bash

mkdir -p ${GRADLE_USER_HOME}
rm -rf ${GRADLE_USER_HOME}/gradle.properties
if [ ! -f ${GRADLE_USER_HOME}/gradle.properties ]
then
    echo 'systemProp.http.proxyHost=proxy' >> ${GRADLE_USER_HOME}/gradle.properties
    echo 'systemProp.http.proxyPort=8080' >> ${GRADLE_USER_HOME}/gradle.properties
    echo 'systemProp.https.proxyHost=proxy' >> ${GRADLE_USER_HOME}/gradle.properties
    echo 'systemProp.https.proxyPort=8080' >> ${GRADLE_USER_HOME}/gradle.properties
fi


