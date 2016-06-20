#!/usr/bin/env bash
add-apt-repository -y ppa:webupd8team/java

apt-get install -y oracle-java8-installer
apt-get -y install redis-server

service yadel.cli stop
apt-get -y remove yadel.cli
rm -rf /opt/yadel.cli

dpkg-deb --build yadel.cli_${YADEL_VERSION}_all
dpkg -i yadel.cli_${YADEL_VERSION}_all.deb
apt-get update
apt-get install -f -y
dpkg -i yadel.cli_${YADEL_VERSION}_all.deb