# NICTA t3as SNOMED service common classes

A library containing classes which are used across the different service modules. Among other things it has a set of Java classes auto-generated from the MetaMap XML DTD using the Java XML Binding Compiler (`xjc`).

This project is also available on Maven Central under the `org.t3as` group id, as artifact id `snomed-common`:

<http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22snomed-common%22>

## License

Copyright (c) 2014, National ICT Australia
All rights reserved.

This software is under the GPL version 3.
Please see the license file LICENSE.txt

Alternative licensing can be arranged, please contact us.

## MetaMap XML JAXB bindings

The DTD is available here:

<http://metamap.nlm.nih.gov/DTD/MMOtoXML_v5.dtd>

A command similar to this will generate the JAXB annotated Java sources from the XML DTD:

    xjc -d src/main/java/ -p org.t3as.metamap.jaxb -dtd MMOtoXML_v5.dtd
