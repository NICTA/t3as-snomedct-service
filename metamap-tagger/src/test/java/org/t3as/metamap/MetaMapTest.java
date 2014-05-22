package org.t3as.metamap;

/*
 * #%L
 * MetaMap Tagger
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

import java.util.Collections;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.t3as.metamap.MetaMap.sanitiseSemanticTypes;

public class MetaMapTest {

    @Test
    public void testDecomposeToAscii() throws Exception {
        assertEquals("xyaaaoooxy", MetaMap.decomposeToAscii("xyâåäöốởxy"));
        assertEquals("a  b", MetaMap.decomposeToAscii("a µ b"));
        assertEquals("1234567890abcdefghijklmnopqrstuvwxyz\"!@#$%^&*()?",
                     MetaMap.decomposeToAscii("1234567890abcdefghijklmnopqrstuvwxyz\"!@#$%^&*()?"));
        assertEquals("", MetaMap.decomposeToAscii("Ƣ"));
        assertEquals("ae", MetaMap.decomposeToAscii("ǽ"));
        assertEquals("O", MetaMap.decomposeToAscii("Ǿ"));
        assertEquals("t", MetaMap.decomposeToAscii("ȶ"));
    }

    @Test
    public void testSanitiseSemanticTypes() {
        assertEquals(MetaMap.DEFAULT_MM_SEMANTIC_TYPES, sanitiseSemanticTypes(null));
        assertEquals(MetaMap.DEFAULT_MM_SEMANTIC_TYPES, sanitiseSemanticTypes(Collections.<String>emptyList()));
        assertEquals(MetaMap.DEFAULT_MM_SEMANTIC_TYPES, sanitiseSemanticTypes(Collections.singleton("foobar")));

        assertEquals(asList("dsyn"), sanitiseSemanticTypes(Collections.singleton("dsyn")));
        assertEquals(asList("dsyn"), sanitiseSemanticTypes(asList("dsyn", "foobar")));
        assertEquals(asList("dsyn", "fish"), sanitiseSemanticTypes(asList("dsyn", "foobar", "fish")));
    }
}
