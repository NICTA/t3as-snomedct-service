# NICTA t3as SNOMED CT Coder Web Service

This project contains a web service, and associated tools and services, for analysing some free text and returning matching SNOMED CT codes. To do this it makes use of [NLM MetaMap](http://metamap.nlm.nih.gov/) and [NLM UMLS](http://www.nlm.nih.gov/research/umls/).

This code is under the GPLv3 license. Alternative licensing can be arranged, please contact us.

## Sub-projects

These are the subprojects:

1. [MetaMap Tagger](metamap-tagger)
2. [MetaMap XML](metamap-xml)
3. [SNOMED CT Lookup](snomedct-lookup)
4. [SNOMED CT Coder Web Service](snomed-coder-web)
5. [SNOMED CT Coder Web User Interface](snomed-coder-ui-web)

# Installation

For operation of the web service the first three subprojects must be properly installed and configured, the User Interface is optional.

# Building

Simply run Maven without arguments in the top level directory:

    mvn

This downloads all Java dependencies and builds all of the subprojects. (NLM MetaMap and UMLS are external dependencies that you will have to install manually, see subprojects.) Two WAR files are generated, one in `snomed-coder-web/target/`, and another in `snomed-coder-ui-web/target/` (optional). Please also see the installation instructions for the MetaMap Tagger and the SNOMED CT Lookup projects, as these are required for proper operation.
