# NICTA t3as SNOMED CT Coder Web Service

This project contains a web service, and associated tools and services, for analysing some free text and returning matching SNOMED CT codes. To do this it makes use of [NLM MetaMap](http://metamap.nlm.nih.gov/) and [NLM UMLS](http://www.nlm.nih.gov/research/umls/).

This project is funded by National ICT Australia ([NICTA](http://nicta.com.au/)), and is developed by the Text Analytics as a Service ([t3as](http://t3as.org/)) team. We run a demo of this service at:
 
<http://snomedct.t3as.org/>

Parts of this project are also available on Maven Central under the `org.t3as` group id:

<http://search.maven.org/#search%7Cga%7C1%7Corg.t3as>

## License

Copyright (c) 2014, National ICT Australia
All rights reserved.

This software is under the GPL version 3.
Please see the license file LICENSE.txt

Alternative licensing can be arranged, please contact us.

## Sub-projects

These are the subprojects:

1. [MetaMap Tagger](metamap-tagger)
2. [Service Common Classes](snomed-coder-common)
3. [SNOMED CT Lookup](snomedct-lookup)
4. [SNOMED CT Coder Web Service](snomed-coder-web)
5. [SNOMED CT Coder Web User Interface](snomed-coder-ui-web)
6. [SNOMED CT service Java REST client](snomed-coder-client)

# Installation

For operation of the web service the `MetaMap Tagger`, `SNOMED CT Lookup`, and the `SNOMED CT Coder Web Service` projects must be installed and configured. The `SNOMED CT Coder Web User Interface` project is optional.

# Building

Simply run Maven without arguments in the top level directory:

    mvn

This downloads all Java dependencies and builds all of the subprojects. (NLM MetaMap and UMLS are external dependencies that you will have to install manually, see subprojects.) Two WAR files are generated, one in `snomed-coder-web/target/`, and another in `snomed-coder-ui-web/target/` (optional). Please also see the installation instructions for the MetaMap Tagger and the SNOMED CT Lookup projects, as these are required for proper operation.
