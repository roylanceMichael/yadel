#!/usr/bin/env bash
unamestr=$(uname)

if [[ "$unamestr" == 'Darwin' ]]; then
    ~/.nuget/packages/Google.Protobuf.Tools/3.0.0/tools/macosx_x64/protoc -I=src/main/resources --proto_path=src/main/resources --java_out=src/main/java src/main/resources/*.proto
else
    ~/.nuget/packages/Google.Protobuf.Tools/3.0.0/tools/linux_x64/protoc -I=src/main/resources --proto_path=src/main/resources --java_out=src/main/java src/main/resources/*.proto
fi