#!/usr/bin/env bash
if [ -d "/app/.jdk" ]; then
    export JAVA_HOME=/app/.jdk
fi

sh target/bin/webapp
