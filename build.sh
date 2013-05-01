#!/bin/bash
mvn clean compile assembly:single

cp ./target/strings_from_apk-0.0.1-SNAPSHOT-jar-with-all.jar ./strings_from_apk.jar