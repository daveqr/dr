# Readme
## Overview
This app is a demonstration of a rudimentary natural language processing library. It

* Parses natural lange text into sentences
* Identifies specified keywords
* Converts the resulting model into XML and prints the result to System.out

## Requirements

* Java 8
* Maven 3

## Usage
``` bash
$  mvn spring-boot:run
```

By default, the app will load it's dataset from the zip file on the classpath.

## Techniques Demonstrated

* Natural language processing
* Concurrency with CompletableFutures
* Reading and processing Zip files
* Spring integration
* In-memory database
* XML bindings with JAXB