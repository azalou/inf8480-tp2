#!/bin/bash

pushd $(dirname $0) > /dev/null
#basepath=$(pwd)
popd > /dev/null


java -cp serviceDeNom.jar:sharedinterfaces.jar \
        -Djava.rmi.server.codebase=file:sharedinterfaces.jar \
        -Djava.security.policy=policy \
        ca.inf8480.tp2.serviceDeNom.ServiceDeNom
