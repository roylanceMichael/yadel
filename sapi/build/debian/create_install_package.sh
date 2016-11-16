#!/usr/bin/env bash
apt-get install --reinstall ca-certificates
add-apt-repository -y ppa:webupd8team/java
echo debconf shared/accepted-oracle-license-v1-1 select true | \
  debconf-set-selections
echo debconf shared/accepted-oracle-license-v1-1 seen true | \
  debconf-set-selections
apt-get install -y oracle-java8-installer
service sapi stop
apt-get -y remove sapi
rm -rf /opt/sapi
dpkg-deb --build sapi_0.147_all
dpkg -i sapi_0.147_all.deb
apt-get update
apt-get install -f -y
dpkg -i sapi_0.147_all.deb
