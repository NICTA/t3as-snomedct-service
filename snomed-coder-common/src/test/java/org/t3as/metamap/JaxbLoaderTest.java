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
 * it with H2, GWT, or JavaBeans Activation Framework (JAF) (or a
 * modified version of those libraries), containing parts covered by the
 * terms of the H2 License, the GWT Terms, or the Common Development and
 * Distribution License (CDDL) version 1.0 ,the licensors of this Program
 * grant you additional permission to convey the resulting work.
 * #L%
 */
package org.t3as.metamap;

import org.t3as.metamap.jaxb.Candidate;
import org.t3as.metamap.jaxb.MMO;
import org.t3as.metamap.jaxb.MMOs;
import org.t3as.metamap.jaxb.Mapping;
import org.t3as.metamap.jaxb.Phrase;
import org.t3as.metamap.jaxb.SemType;
import org.t3as.metamap.jaxb.Utterance;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

public class JaxbLoaderTest {

    private static final File TEST_XML = new File("src/test/resources/test.xml");

    @SuppressWarnings("OverlyBroadThrowsClause")
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
        assertEquals(candidate.getCandidateCUI(), "C0004096");
        assertEquals(candidate.getCandidatePreferred(), "Asthma");

        final SemType semType = candidate.getSemTypes().getSemType().get(0);
        assertEquals(semType.getvalue(), "dsyn");
    }

    @Test
    public void failToLoad() {
        //noinspection OverlyBroadCatchBlock
        try {
            JaxbLoader.loadXml(new File("noSuchFile"));
            fail("We should have had an exception before now.");
        }
        catch (final FileNotFoundException ignored) {
            // do nothing
        }
        catch (final Throwable t) {
            //noinspection CallToPrintStackTrace
            t.printStackTrace();
            fail("There should be no other throwables.");
        }
    }
}
