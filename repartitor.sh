#!/bin/bash

pushd $(dirname $0) > /dev/null
basepath=$(pwd)
popd > /dev/null

java -cp "$basepath"/repartiteur.jar:"$basepath"/sharedinterfaces.jar \
        -Djava.rmi.server.codebase=file:"$basepath"/sharedinterfaces.jar \
        -Djava.security.policy="$basepath"/policy \
        ca.inf8480.tp2.repartiteur.Repartitor
