#!/bin/bash
set -euo pipefail
cd "$(dirname "$0")"
export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)
./gradlew --no-daemon clean test checkstyleTest
open build/reports/tests/test/index.html
