# NICTA t3as MetaMap XML Java representation

A library containing mostly Java classes auto-generated from the MetaMap XML DTD using the Java XML Binding Compiler (`xjc`).

The DTD is available here:

<http://metamap.nlm.nih.gov/DTD/MMOtoXML_v5.dtd>

A command similar to this will generate the sources:

    xjc -d src/main/java/ -p org.t3as.metamap.jaxb -dtd MMOtoXML_v5.dtd
