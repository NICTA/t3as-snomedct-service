package org.t3as.metamap;

/*
 * #%L
 * NICTA t3as SNOMED service common classes
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

import org.junit.Test;
import org.t3as.metamap.jaxb.Candidate;
import org.t3as.metamap.jaxb.MMO;
import org.t3as.metamap.jaxb.MMOs;
import org.t3as.metamap.jaxb.Mapping;
import org.t3as.metamap.jaxb.Phrase;
import org.t3as.metamap.jaxb.SemType;
import org.t3as.metamap.jaxb.Utterance;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class JaxbLoaderTest {

    private static final File TEST_XML = new File("src/test/resources/test.xml");

    @Test
    public void testLoadXml() throws Exception {
        final MMOs root = JaxbLoader.loadXml(TEST_XML);
        assertNotNull(root);

        final MMO mmo = root.getMMO().get(0);
        assertNotNull(mmo);

        final Utterance utterance = mmo.getUtterances().getUtterance().get(0);
        assertNotNull(utterance);

        final Phrase phrase = utterance.getPhrases().getPhrase().get(0);
        assertNotNull(phrase);

        final Mapping mapping = phrase.getMappings().getMapping().get(0);
        assertNotNull(mapping);

        final Candidate candidate = mapping.getMappingCandidates().getCandidate().get(0);
        assertNotNull(candidate);
        assertEquals("C0004096", candidate.getCandidateCUI());
        assertEquals("Asthma", candidate.getCandidatePreferred());

        final SemType semType = candidate.getSemTypes().getSemType().get(0);
        assertEquals("dsyn", semType.getvalue());
    }

    @Test
    public void failToLoad() {
        try {
            JaxbLoader.loadXml(new File("noSuchFile"));
            fail("We should have had an exception before now.");
        }
        catch (final FileNotFoundException e) {
            // do nothing
        }
        catch (final Throwable t) {
            t.printStackTrace();
            fail("There should be no other throwables.");
        }
    }

    @Test
    public void testLoadResource() throws Exception {
        final String[] s = JaxbLoader.loadResource("test.xml");
        assertTrue(s[0].startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
    }
}