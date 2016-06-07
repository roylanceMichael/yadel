#!/usr/bin/env bash
rm -rf build
./gradlew installApp
cp -r scripts/* build/install/yadel.cli
chmod a+x+r build/install/yadel.cli/run.sh
chmod a+x+r build/install/yadel.cli/run.bat

pushd build/install
zip -r yadel.cli.zip yadel.cli
mv yadel.cli.zip ../../