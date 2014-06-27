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
package org.t3as.snomedct.gwt.client.gwt;

import org.junit.Assert;
import org.junit.Test;

public class SnomedRequestCallbackTest {

    @Test
    public void testNextMappingGroup() throws Exception {
        Assert.assertEquals("A", SnomedRequestCallback.nextMappingGroup(null));
        Assert.assertEquals("A", SnomedRequestCallback.nextMappingGroup(""));

        Assert.assertEquals("B", SnomedRequestCallback.nextMappingGroup("A"));
        Assert.assertEquals("C", SnomedRequestCallback.nextMappingGroup("B"));
        Assert.assertEquals("AA", SnomedRequestCallback.nextMappingGroup("Z"));

        Assert.assertEquals("AB", SnomedRequestCallback.nextMappingGroup("AA"));
        Assert.assertEquals("AD", SnomedRequestCallback.nextMappingGroup("AC"));
        Assert.assertEquals("BA", SnomedRequestCallback.nextMappingGroup("AZ"));

        Assert.assertEquals("BB", SnomedRequestCallback.nextMappingGroup("BA"));
        Assert.assertEquals("AAA", SnomedRequestCallback.nextMappingGroup("ZZ"));

        Assert.assertEquals("AAB", SnomedRequestCallback.nextMappingGroup("AAA"));
        Assert.assertEquals("AZB", SnomedRequestCallback.nextMappingGroup("AZA"));
        Assert.assertEquals("BAA", SnomedRequestCallback.nextMappingGroup("AZZ"));
        Assert.assertEquals("AAAA", SnomedRequestCallback.nextMappingGroup("ZZZ"));

        Assert.assertEquals("ACAA", SnomedRequestCallback.nextMappingGroup("ABZZ"));
    }
}
