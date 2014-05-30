# NICTA t3as SNOMED CT Coder Web Service

This web service takes some free text and tries to determine what SNOMED CT codes can be extracted from it.

This code is under the GPLv3 license. Alternative licensing can be arranged, please contact us.



## Installation

The web service requires that the [MetaMap Tagger](metamap-tagger) and [SNOMED CT Lookup](snomedct-lookup) installation instructions has been followed. Other than that it simply just needs the WAR file to be deployed on a Java Servlet compatible app server.



## Building

You can build the SNOMED CT Coder Web Service project only by entering the `snomed-coder-web` directory and running Maven with no arguments:

    mvn

To make a t3as .tar.gz assembly run:

    mvn assembly:single

This requires that the [MetaMap Tagger](metamap-tagger) and [SNOMED CT Lookup](snomedct-lookup) installation instructions has been followed - i.e. that MetaMap has been installed at `/opt/snomed-coder-web/metamap/` and the SNOMED CT lookup db file exists at `/opt/snomed-coder-web/data/snomedct.h2.db`.



## Running

This project can be run as a Maven development service or using the WAR file in an app server.

There are a number of system properties available to set:

1. **publicMmDir** (default: /opt/snomed-coder-web/metamap/public_mm), path to the MetaMap installation *public_mm* directory.
2. **snomedDbPath** (default: /opt/snomed-coder-web/data/snomedct), path/base name of the SNOMED CT lookup database.



### Using Maven

This project can be run as a Maven development service by entering the `snomed-coder-web` directory and running:

    mvn tomcat7:run



### Using an App Server

To run using an app server, simply copy the WAR file from the `snomed-coder-web/target` directory into the webapp directory of any Java Servlet compatible app server.



## Web service end points

### /snomed-coder-web/rest/v1.0/snomedctCodes

This is a web service to analyse English clinical text and return all the SNOMED CT concepts that could be found. The service can return JSON or XML. By default JSON is returned, but by passing the HTTP `Accept` header you can determine what you want to have returned:

    Accept: application/json

    Accept: text/xml



#### The simple case

To use the web service simply POST some URL encoded text (with the content type `application/x-www-form-urlencoded`)
to this URL, which will return a JSON response. If you are on Linux or OSX a simple test might look like this (note the `python` line is optional and only used to make it more human readable):

    curl -s --request POST \
        -H "Content-Type: application/x-www-form-urlencoded" \
        --data-urlencode "The patient had a stroke." \
        http://snomedct.t3as.org/snomed-coder-web/rest/v1.0/snomedctCodes \
        | python -mjson.tool

To get the answer as XML, pass a suitable `Accept` header (note the `xmllint` line is optional and only used to make it more human readable):

    curl -s --request POST \
        -H "Content-Type: application/x-www-form-urlencoded" \
        -H "Accept: text/xml" \
        --data-urlencode "The patient had a stroke." \
        http://snomedct.t3as.org/snomed-coder-web/rest/v1.0/snomedctCodes \
        | xmllint --format -



#### Specifying custom MetaMap Semantic Types

It is also possible to specify custom Semantic Types for MetaMap, which can be used to filter the categories of concepts that are searched for. To do this you can use the `semanticType` web service documented in the section below, alternatively see this web page which has the current available semantic types:

<http://metamap.nlm.nih.gov/Docs/SemanticTypes_2013AA.txt>

Once you have determined which semantic types to use you can then pass a JSON request containing the text to analyse and the types:

    {
        "semanticTypes": [
            "fndg",
            "dsyn",
            "ftcn"
        ],
        "text": "The patient had a stroke."
    }

When using the web service in this way you must set the `Content-Type` header to JSON:

    curl -s --request POST \
        -H "Content-Type: application/json" \
        --data '{"text": "The patient had a stroke.", "semanticTypes": ["fndg","dsyn","ftcn"]}' \
        http://snomedct.t3as.org/snomed-coder-web/rest/v1.0/snomedctCodes \
        | python -mjson.tool



#### Specifying MetaMap options

It is possible to pass specific runtime options for MetaMap. To do this include `options` in a JSON request:

    {
        "options": [
            "ignore_word_order",
            "composite_phrases 8"
        ],
        "text": "The patient had a stroke.",
    }

Use something like the following for a command line test:

    curl -s --request POST \
        -H "Content-Type: application/json" \
        --data '{"text": "The patient had a stroke.", "options": ["ignore_word_order","composite_phrases 8"]}' \
        http://snomedct.t3as.org/snomed-coder-web/rest/v1.0/snomedctCodes \
        | python -mjson.tool

MetaMap is always run with these minimum options:

    composite_phrases=4
    lexicon=db
    lexicon_year=2013
    mm_data_year=2013AB
    restrict_to_sources=[SNOMEDCT_US]
    XMLf1

If an empty `options` array is passed then no extra options are passed to MetaMap. If no `options` array is passed at all then these default options will be used/changed in addition to the minimum above:

    allow_large_n
    composite_phrases=8
    ignore_word_order
    no_derivational_variants
    strict_model
    word_sense_disambiguation

In addition to these the service currently also supports the following options:

    all_acros_abbrs
    ignore_stop_phrases

Adding more options is relatively trivial (and can be done on request), but does require code changes.



### /snomed-coder-web/rest/v1.0/semanticTypes

Behind the scenes NLM MetaMap is used, and it is possible to specify which MetaMap Semantic Types (STs) that we are
interested in. In order to do so it can be handy to first get a list of the STs that are available, which will also
indicate if it is a type that is enabled by default by the web service if no STs are specifically passed:

    curl -s http://snomedct.t3as.org/snomed-coder-web/rest/v1.0/semanticTypes | python -mjson.tool
