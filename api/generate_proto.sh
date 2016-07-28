#!/usr/bin/env bash
$(which protoc) -I=src/main/resources --proto_path=src/main/resources --java_out=src/main/java src/main/resources/*.proto

#./gradlew build
#
#mkdir -p javascript
#echo "compiling javascript models"
#pushd javascript
#npm install
#node_modules/protobufjs/bin/pbjs ../src/main/resources/yadel_report.proto ../src/main/resources/yadel_model.proto  > model.json
#node_modules/protobufjs/bin/pbjs ../src/main/resources/yadel_report.proto ../src/main/resources/yadel_model.proto -t js > model.js
#node_modules/proto2typescript/bin/proto2typescript-bin.js --file model.json > model.d.ts
#node_modules/@mroylance/protobuftshelper/run.sh model.js model_factories.ts ./model.d.ts YadelModel
#rm -rf model.json
#rm -rf model.js
#popd
#
#echo "building java client (capi), java server (sapi), and typescript services"
#echo "publishing typescript"
#pushd javascript
##npm publish
#popd
#
#echo "building and publishing capi"
#pushd ../capi
#chmod -R 777 .
#gradle build
## gradle artifactoryPublish
#popd
#
#echo "building sapi (no need to publish)"
#pushd ../sapi
#chmod -R 777 .
#mvn compile
#mvn package