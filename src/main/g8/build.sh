#!/usr/bin/env bash -eu
./sbt stage \
&& docker build -t $name;format="lower,word"$ .