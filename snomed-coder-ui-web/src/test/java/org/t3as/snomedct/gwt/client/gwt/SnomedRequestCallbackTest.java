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
 * it with H2, GWT, or JavaBeans Activation Framework (JAF) (or a
 * modified version of those libraries), containing parts covered by the
 * terms of the H2 License, the GWT Terms, or the Common Development and
 * Distribution License (CDDL) version 1.0 ,the licensors of this Program
 * grant you additional permission to convey the resulting work.
 * #L%
 */
package org.t3as.snomedct.gwt.client.gwt;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class SnomedRequestCallbackTest {

    @Test
    public void testNextMappingGroup() throws Exception {
        assertEquals("A", SnomedRequestCallback.nextMappingGroup(null));
        assertEquals("A", SnomedRequestCallback.nextMappingGroup(""));

        assertEquals("B", SnomedRequestCallback.nextMappingGroup("A"));
        assertEquals("C", SnomedRequestCallback.nextMappingGroup("B"));
        assertEquals("AA", SnomedRequestCallback.nextMappingGroup("Z"));

        assertEquals("AB", SnomedRequestCallback.nextMappingGroup("AA"));
        assertEquals("AD", SnomedRequestCallback.nextMappingGroup("AC"));
        assertEquals("BA", SnomedRequestCallback.nextMappingGroup("AZ"));

        assertEquals("BB", SnomedRequestCallback.nextMappingGroup("BA"));
        assertEquals("AAA", SnomedRequestCallback.nextMappingGroup("ZZ"));

        assertEquals("AAB", SnomedRequestCallback.nextMappingGroup("AAA"));
        assertEquals("AZB", SnomedRequestCallback.nextMappingGroup("AZA"));
        assertEquals("BAA", SnomedRequestCallback.nextMappingGroup("AZZ"));
        assertEquals("AAAA", SnomedRequestCallback.nextMappingGroup("ZZZ"));

        assertEquals("ACAA", SnomedRequestCallback.nextMappingGroup("ABZZ"));
    }
}
