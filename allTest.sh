#!/usr/bin/env bash

source ~/.profile
./config/travis/run-checks.sh &&
./gradlew clean checkstyleMain checkstyleTest headless allTests coverage coveralls asciidoctor copyDummySearchPage
