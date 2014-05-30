package org.t3as.metamap;

/*
 * #%L
 * NICTA t3as MetaMap XML JAXB
 * %%
 * Copyright (C) 2014 NICTA
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * 
 * Additional permission under GNU GPL version 3 section 7
 * 
 * If you modify this Program, or any covered work, by linking or combining
 * it with H2, GWT, JUnit, or JavaBeans Activation Framework (JAF) (or a
 * modified version of those libraries), containing parts covered by the
 * terms of the H2 License, the GWT Terms, the Common Public License
 * Version 1.0, or the Common Development and Distribution License (CDDL)
 * version 1.0 ,the licensors of this Program grant you additional
 * permission to convey the resulting work.
 * #L%
 */

import org.t3as.metamap.jaxb.MMOs;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

public class JaxbLoader {

    public static MMOs loadXml(final File xmlFile)
            throws JAXBException, SAXException, ParserConfigurationException, FileNotFoundException {
        final JAXBContext jaxbContext = JAXBContext.newInstance(MMOs.class);
        final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        // avoid getting the DTD (was mainly for when demoing without a network, but should maybe be kept?)
        final SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setFeature("http://apache.org/xml/features/validation/schema", false);
        spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        final XMLReader xmlReader = spf.newSAXParser().getXMLReader();
        final InputSource inputSource = new InputSource(new FileReader(xmlFile));
        final SAXSource source = new SAXSource(xmlReader, inputSource);

        return (MMOs) jaxbUnmarshaller.unmarshal(source);
    }

    /** Why here? Why not here? */
    public static String[] loadResource(final String fileResource) throws IOException {
        final Collection<String> lines = new ArrayList<>();
        //noinspection ConstantConditions
        try (final BufferedReader r = new BufferedReader(new InputStreamReader(
                JaxbLoader.class.getClassLoader().getResource(fileResource).openStream()))
        ) {
            for (String line; (line = r.readLine()) != null; ) {
                lines.add(line);
            }
        }
        return lines.toArray(new String[lines.size()]);
    }
}
