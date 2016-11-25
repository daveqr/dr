# Readme

## Requirements

This library requires the following:

* Java 8
* Maven 3

## Installation

Clone the project from GitHub.

    $ git clone https://github.com/daveqr/dr.git

## Usage
From the project directory, run:

    $ mvn exec:java -Dexec.mainClass="com.davedaniels.dr.Main"

## Running Tests
From the project directory, run:

    $ mvn test

## Assumptions

* The file exists on the Classpath.
* The file contains data, and the data meets reasonable constraint expectations.
* The default Locale is US.
* Helper methods in the service are protected for testing.
* The sentence boundary and word tokenization tests are limited. There are probably more conditions which should be tested, including null tests.

## Limitations

One major limitation to the current implementation is the service expects the source file to be at the root of the Classpath. This restricts its usefulness in real-world scenarios, where data might be from a database, a generated String or any number of other sources, and might be used in different contexts. As it is written the NlpService serves mainly to load the model and print the XML.