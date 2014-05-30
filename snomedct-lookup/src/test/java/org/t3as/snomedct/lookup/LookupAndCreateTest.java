package org.t3as.snomedct.lookup;

/*
 * #%L
 * NICTA t3as UMLS CUI to SNOMED CT Lookup
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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.t3as.metamap.JaxbLoader;
import org.t3as.metamap.jaxb.Candidate;
import org.t3as.metamap.jaxb.MMOs;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LookupAndCreateTest {

    private static final File TEST_RESOURCES_DIR = new File("src/test/resources");
    private static final File DB_DIR = new File("target/test_db");
    private static final File DB_FILE_PREFIX = new File(DB_DIR, "test_snomedct");
    private static final File DB_FILE = new File(DB_DIR, "test_snomedct.h2.db");
    private static final File XML_INPUT = new File(TEST_RESOURCES_DIR, "test.xml");

    @Before
    public void prepareDb() throws Exception {
        //noinspection ResultOfMethodCallIgnored
        DB_DIR.mkdir();
        final long start = System.currentTimeMillis();
        try (final CreateLookupDb createLookupDb = new CreateLookupDb(DB_FILE_PREFIX, TEST_RESOURCES_DIR)) {
            final int concepts = createLookupDb.process();
            final long end = System.currentTimeMillis();
            assertTrue(DB_FILE.exists());
            DB_FILE.deleteOnExit();
            System.out.printf("Loaded %,d SNOMED DB concepts in %,d millis.\n", concepts, end - start);
        }
    }

    @After
    public void removeDb() {
        assertTrue(DB_FILE.delete());
    }

    @Test
    public void testAddSnomedId() throws Exception {
        final Candidate found1 = new Candidate();
        found1.setCandidateCUI("C0024117");
        found1.setCandidatePreferred("Chronic obstructive pulmonary disease NOS");

        final Candidate found2 = new Candidate();
        found2.setCandidateCUI("C0007287");
        found2.setCandidatePreferred("Carpet");

        final Candidate notFound = new Candidate();
        notFound.setCandidateCUI("C0001234");
        notFound.setCandidatePreferred("Expected not to be found");

        try (final SnomedLookup snomedLookup = new SnomedLookup(DB_FILE_PREFIX)) {
            assertTrue(snomedLookup.addSnomedId(found1));
            assertEquals("196003006", found1.getSnomedId());

            assertFalse(snomedLookup.addSnomedId(notFound));
            assertNull(notFound.getSnomedId());

            assertTrue(snomedLookup.addSnomedId(found2));
            assertEquals("5541000124106", found2.getSnomedId());
        }
    }

    @Test
    public void testEnrichXml() throws Exception {
        final MMOs root = JaxbLoader.loadXml(XML_INPUT);
        assertNotNull(root);

        final Candidate candidate = root.getMMO().get(0)
                                        .getUtterances().getUtterance().get(0)
                                        .getPhrases().getPhrase().get(0)
                                        .getMappings().getMapping().get(0)
                                        .getMappingCandidates().getCandidate().get(0);
        assertEquals("C2712160", candidate.getCandidateCUI());
        assertEquals("Normal nervous system function", candidate.getCandidatePreferred());
        assertNull(candidate.getSnomedId());

        try (final SnomedLookup snomedLookup = new SnomedLookup(DB_FILE_PREFIX)) {
            snomedLookup.enrichXml(root);
        }

        assertEquals("18460000", candidate.getSnomedId());
    }

    @Test
    public void testFindSnomedConcept() throws SQLException, IOException {
        try (final SnomedLookup snomedLookup = new SnomedLookup(DB_FILE_PREFIX)) {
            final SnomedTerm result = snomedLookup.findFromCuiAndDesc("C2712160", "Normal nervous system function");
            assertEquals("18460000", result.snomedId);
            assertEquals("PT", result.termType);
        }
    }
}
