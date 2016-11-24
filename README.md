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
* The requirement is to identify sentence boundaries, not to parse the sentences into Strings. However, that requirement would only be one step further, and the boundaries could be used to split the String into sentences.
* Helper methods in the service are protected for testing.
* The sentence boundary and word tokenization tests are limited. There are probably more conditions which should be tested.

## Implementation and Limitations

The implementation is straight-forward, using a transaction script to process the file. With a little bit of work the service could be made into a template so that different implementations could handle, for example, non-US Locales.
