package org.t3as.snomedct.gwt.client.snomed;

/*
 * #%L
 * NICTA t3as SNOMED CT GWT UI
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.t3as.snomedct.gwt.client.snomed.TestData.unmodifiableTestData;

public class SnomedDescriptionComparatorTest {

    @Test
    public void testCompare() throws Exception {
        // sort just the descriptions to get the comparative order
        final Collection<String> sortedDescriptionsColl = new TreeSet<>();
        for (final SnomedConcept concept : unmodifiableTestData) {
            sortedDescriptionsColl.add(concept.candidatePreferred);
        }
        final List<String> sortedDescriptions = new ArrayList<>(sortedDescriptionsColl);

        // then use the comparator
        final List<SnomedConcept> sorted = new ArrayList<>(unmodifiableTestData);
        Collections.sort(sorted, new SnomedDescriptionComparator());

        // then check they concur
        for (int i = 0; i < sorted.size(); i++) {
            assertEquals(sortedDescriptions.get(i), sorted.get(i).candidatePreferred);
        }
    }
}
