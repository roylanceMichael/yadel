#!/usr/bin/env bash
export YACLIB_VERSION=76
cat >gradle.properties <<EOL
currentVersion=0.${YACLIB_VERSION}-SNAPSHOT
EOL

pushd ..
export YACLIB_LOCATION=$(pwd)
popd

echo "make sure that javascript version, this version, and version under test are the same!"
protoc -I=src/main/resources --proto_path=src/main/resources --java_out=src/main/java src/main/resources/*.proto

mkdir -p javascript
echo "compiling javascript models"
pushd javascript

mv node_modules ../
find . -name "*.ts" -type f -delete
find . -name "*.map" -type f -delete
find . -name "*.js" -type f -delete
mv ../node_modules .

npm install
node_modules/protobufjs/bin/pbjs ../src/main/resources/yadel_model.proto ../src/main/resources/yadel_report.proto  > model.json
node_modules/protobufjs/bin/pbjs ../src/main/resources/yadel_model.proto ../src/main/resources/yadel_report.proto -t js > model.js
node_modules/proto2typescript/bin/proto2typescript-bin.js --file model.json > model.d.ts
node_modules/@mroylance/protobuftshelper/run.sh model.js model_factories.ts ./model.d.ts YadelModel
rm -rf model.json
rm -rf model.js
popd

pushd ..
rm -rf capi
popd

echo "building java client (capi), java server (sapi), and typescript services"
gradle clean
./gradlew build

echo "publishing typescript"
pushd javascript
ls *.ts > ts-files.txt
tsc @ts-files.txt
rm -rf ts-files.txt
npm version 0.0.${YACLIB_VERSION}
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
chmod -R 777 .
mvn compile
mvn package