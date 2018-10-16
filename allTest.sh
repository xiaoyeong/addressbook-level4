#!/usr/bin/env bash

#./config/travis/run-checks.sh
./gradlew clean checkstyleMain checkstyleTest headless allTests coverage coveralls asciidoctor copyDummySearchPage