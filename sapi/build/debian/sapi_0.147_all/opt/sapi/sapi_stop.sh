#!/usr/bin/env bash
kill -9 $(lsof -i:8080 -t)
