#!/usr/bin/env bash
export TYPESCRIPT_MODEL_FILE_NAME=YadelModel
export YACLIB_VERSION=85

# autogenerate location
pushd ..
export YACLIB_LOCATION=$(pwd)
popd

cat >gradle.properties <<EOL
currentVersion=0.${YACLIB_VERSION}-SNAPSHOT
EOL

protoc -I=src/main/resources --proto_path=src/main/resources --java_out=src/main/java src/main/resources/*.proto

# remove javascript and capi folders, they'll be recreated
rm -rf javascript
rm -rf ../capi

echo "building java client (capi), java server (sapi), and typescript services"
./gradlew clean
./gradlew build

pushd javascript
npm install
node_modules/protobufjs/bin/pbjs ../src/main/resources/*.proto  > model.json
node_modules/protobufjs/bin/pbjs ../src/main/resources/*.proto -t js > model.js
node_modules/proto2typescript/bin/proto2typescript-bin.js --file model.json > ${TYPESCRIPT_MODEL_FILE_NAME}.d.ts
node_modules/@mroylance/protobuftshelper/run.sh model.js ${TYPESCRIPT_MODEL_FILE_NAME}Factory.ts ./${TYPESCRIPT_MODEL_FILE_NAME}.d.ts ${TYPESCRIPT_MODEL_FILE_NAME}
rm -rf model.json
rm -rf model.js
popd

echo "publishing typescript"
pushd javascript
ls *.ts > ts-files.txt
tsc @ts-files.txt
rm -rf ts-files.txt
npm publish
popd

gradle artifactoryPublish

echo "building and publishing capi"
pushd ../capi
gradle clean
gradle build
gradle artifactoryPublish
popd

echo "building sapi (no need to publish)"
pushd ../sapi
mvn compile
mvn package
pushd src/main/javascript
npm install
gulp