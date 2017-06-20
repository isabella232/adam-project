#!/usr/bin/env bash

ANDROID_OPTIONS="--proxy-host proxy --proxy-port 8080 --no-ui --no-https --all"
if [ ! -d ${ANDROID_HOME}/extras/android/m2repository/com/android/support/appcompat-v7/${ANDROID_SUPPORT_VERSION} ]
then
    echo y | android --silent update sdk ${ANDROID_OPTIONS} --filter "extra-android-m2repository"
fi
