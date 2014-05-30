# NICTA t3as MetaMap Tagger

The t3as MetaMap Tagger is a wrapper around [NLM MetaMap](http://metamap.nlm.nih.gov/). It can be used to identify medical (and other) concepts in text.

This code is under the GPLv3 license. Alternative licensing can be arranged, please contact us.

## Installation

Download and install MetaMap for your OS from the link above. To make things as simple as possible and make use of all the pre-defined options then install MetaMap at the path `/opt/snomed-coder-web/metamap/`. If running the [SNOMED CT Coder web service](../snomed-coder-web) then the MetaMap installation must be available locally to the web service.

## Building

You can build the MetaMap Tagger project only by entering the `metamap-tagger` directory and running Maven with no arguments:

    mvn

## Running

In order to use NLM MetaMap two services must be started. To do this change into the MetaMap `public_mm` directory, and then run the following commands:

    bin/skrmedpostctl start
    bin/wsdserverctl start

You may also want to change the amount of memory allocated to the MetaMap Word Sense Disambiguation server, which you do by changing the `-Xmx` parameter in the `bin/wsdserverctl`. The default is 2g, but I've seen some good performance increases by going to 4g or 8g.

It is possible to run the MetaMap Tagger from the command line. Enter the `metamap-tagger` directory, and run the following command to see the available options:

    ./metamap-tagger --help

Note that the output file is the full MetaMap XMLf1 output, but the main interesting output comes out on stdout. The stdout data can then be piped or saved to be used when running the [SNOMED CT Lookup](../snomedct-lookup) tool.

Note that NLM MetaMap requires all text to be 7bit US-ASCII (a.k.a. Unicode Latin1). The SNOMED CT Coder web service will try to simplify all text passed to it to turn complex multibyte characters like 'åäö' into simple ASCII characters like 'aao', but if this involves anything more complicated than just removing a diacritic then the results can be surprising.
