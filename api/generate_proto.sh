#!/usr/bin/env bash
$(which protoc) -I=src/main/resources --proto_path=src/main/resources --java_out=src/main/java src/main/resources/*.proto

../js/node_modules/protobufjs/bin/pbjs src/main/resources/YadelReports.proto > ../js/yadelreports.json
../js/node_modules/protobufjs/bin/pbjs src/main/resources/YadelReports.proto -t js > ../js/yadelreports.js
../js/node_modules/protobufjs/bin/pbjs src/main/resources/YadelReports.proto -t amd > ../js/yadelreports.amd.js
../js/node_modules/protobufjs/bin/pbjs src/main/resources/YadelReports.proto -t commonjs > ../js/yadelreports.common.js
../js/node_modules/proto2typescript/bin/proto2typescript-bin.js --file ../js/yadelreports.json > ../js/yadelreports.d.ts
