# NICTA t3as SNOMED CT Coder Web User Interface

This is a simple interface for using the SNOMED CT Coder web service in a web browser.

## Installation

This app requires the SNOMED CT Coder web service to be running somewhere, but it doesn't necessarily have to be on the same server (the SNOMED CT Coder web service allows Cross Origin Resource Sharing).

## Building

You can build the SNOMED CT Coder Web User Interface project only by entering the `snomed-coder-ui-web` directory and running Maven with no arguments:

    mvn

To make a t3as .tar.gz assembly run:

    mvn assembly:single

## Running

Simply run the WAR file in a Java Servlet compatible app server. By default it is assumed that the SNOMED Coder Web Service is running at on the same host as where the UI webapp is hosted. If this is not the case then run the app server with the following system property (the port is optional):

    -DwebserviceHost=host:port
