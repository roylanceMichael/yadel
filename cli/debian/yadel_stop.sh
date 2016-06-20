#!/usr/bin/env bash
killPid() {
    kill -9 $1 >/dev/null 2>/dev/null
}
for pid in $(ps auxww | grep yadel.cli | grep -v grep | awk '{print $2}'); do killPid ${pid}; done