#!/usr/bin/env bash
pushd /opt/sapi
bash /opt/sapi/custom.sh
nohup /opt/sapi/bin/sapi "$@" > latest.out 2>&1&
