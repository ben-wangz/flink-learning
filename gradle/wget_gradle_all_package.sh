#! /bin/bash

set -x
set -e
set -o pipefail

SCRIPT_PATH="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
PROJECT_PATH=$SCRIPT_PATH/..

PKG_PATH=$PROJECT_PATH/gradle/pkg
mkdir -p $PKG_PATH
GRADLE_ALL_PACKAGE=$PKG_PATH/gradle-6.1.1-all.zip
GRADLE_DOWNLOAD_URL=https://services.gradle.org/distributions/gradle-6.1.1-all.zip
MD5SUM_COMMAND=""
EXTRACT_MD5SUM_COMMAND=""
DOWNLOAD_GRADLE_COMMAND=""
if [[ "$OSTYPE" == "linux-gnu"* ]]; then
    MD5SUM_COMMAND="md5sum ${GRADLE_ALL_PACKAGE}"
    EXTRACT_MD5SUM_COMMAND="awk '{print \$1}'"
    DOWNLOAD_GRADLE_COMMAND="wget -O ${GRADLE_ALL_PACKAGE} ${GRADLE_DOWNLOAD_URL}"
elif [[ "$OSTYPE" == "darwin"* ]]; then
    MD5SUM_COMMAND="md5 ${GRADLE_ALL_PACKAGE}"
    EXTRACT_MD5SUM_COMMAND="awk '{print \$4}'"
    DOWNLOAD_GRADLE_COMMAND="curl -o ${GRADLE_ALL_PACKAGE} -Lk ${GRADLE_DOWNLOAD_URL}"
elif [[ "$OSTYPE" == "cygwin" ]]; then
    echo not yet support cygwin
    exit -1
elif [[ "$OSTYPE" == "msys" ]]; then
    echo not yet support msys
    exit -1
elif [[ "$OSTYPE" == "win32" ]]; then
    echo not yet support win32
    exit -1
elif [[ "$OSTYPE" == "freebsd"* ]]; then
    echo not yet support freebsd
    exit -1
else
    echo unknown os system $OSTYPE
    exit -1
fi

if [[ -f "${GRADLE_ALL_PACKAGE}" ]]; then
    MD5_OF_GRADLE_ALL_PACKAGE=$(bash -c "${MD5SUM_COMMAND} | ${EXTRACT_MD5SUM_COMMAND}")
    if [[ "d1df7d94816bbedb2ab45187fceda7e5" == "${MD5_OF_GRADLE_ALL_PACKAGE}" ]]; then
        echo "${GRADLE_ALL_PACKAGE} already exists, md5 check passed: ${MD5_OF_GRADLE_ALL_PACKAGE}"
    else
        echo "${GRADLE_ALL_PACKAGE} already exists, but md5 check failed: ${MD5_OF_GRADLE_ALL_PACKAGE}"
        rm -f ${GRADLE_ALL_PACKAGE}
        ${DOWNLOAD_GRADLE_COMMAND}
    fi
else
    ${DOWNLOAD_GRADLE_COMMAND}
fi
