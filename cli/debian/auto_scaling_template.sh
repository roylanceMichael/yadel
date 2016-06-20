#!/usr/bin/env bash

add-apt-repository -y ppa:webupd8team/java

echo debconf shared/accepted-oracle-license-v1-1 select true | \
  sudo debconf-set-selections
echo debconf shared/accepted-oracle-license-v1-1 seen true | \
  sudo debconf-set-selections

sudo apt-get update
apt-get install -y oracle-java8-installer

tar -xvf yadel.cli-${YADEL_VERSION}.tar
dpkg-deb --build yadel.cli_${YADEL_VERSION}_all
dpkg -i yadel.cli_${YADEL_VERSION}_all.deb
pushd /opt/yadel.cli
at now + 2 minutes -f /opt/yadel.cli/yadel_actor.sh