# NICTA t3as MetaMap XML Java representation

A library containing mostly Java classes auto-generated from the MetaMap XML DTD using the Java XML Binding Compiler (`xjc`).

This code is under the GPLv3 license. Alternative licensing can be arranged, please contact us.

This project is also available on Maven Central under the `org.t3as` group id, as artifact id `metamap-xml`:

<http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22metamap-xml%22>

The DTD is available here:

<http://metamap.nlm.nih.gov/DTD/MMOtoXML_v5.dtd>

A command similar to this will generate the sources:

    xjc -d src/main/java/ -p org.t3as.metamap.jaxb -dtd MMOtoXML_v5.dtd
