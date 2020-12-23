#! /bin/bash

set -x
set -e
set -o pipefail

SCRIPT_PATH="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
PROJECT_PATH=$SCRIPT_PATH

bash $PROJECT_PATH/gradle/wget_gradle_all_package.sh $@
