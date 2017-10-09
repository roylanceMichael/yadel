#!/usr/bin/env bash
#// This file was auto-generated, but can be altered. It will not be overwritten.
if [ -d "/app/.jdk" ]; then
    export JAVA_HOME=/app/.jdk
fi
source custom.sh
bash bin/yadelserver
