# ygojson-tools

[![build status](https://github.com/ygojson/ygojson-tools/actions/workflows/build.yml/badge.svg?branch=main)](https://github.com/ygojson/ygojson-tools/actions/workflows/build.yml?query=branch%3Amain)
[![codecov](https://codecov.io/gh/ygojson/ygojson-tools/graph/badge.svg?token=FC2VT279O5)](https://codecov.io/gh/ygojson/ygojson-tools)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/2c3210c579f04c6fbd2efe7270a8ee53)](https://app.codacy.com/gh/ygojson/ygojson-tools/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)

Tooling for the ygojson project.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

When running dev mode, the picocli application is executed and on press of the Enter key, is restarted.
As picocli applications will often require arguments to be passed on the commandline, this is also possible in dev mode via:
```shell script
./mvnw compile quarkus:dev -Dquarkus.args='Quarky'
```

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/ygojson-tools-0.2.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## YAML Config

The Quarkus application configuration is located in `src/main/resources/application.yml`.


