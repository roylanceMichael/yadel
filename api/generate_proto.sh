#!/usr/bin/env bash
protoc -I=src/main/proto --proto_path=src/main/proto --java_out=src/main/java src/main/proto/*.proto