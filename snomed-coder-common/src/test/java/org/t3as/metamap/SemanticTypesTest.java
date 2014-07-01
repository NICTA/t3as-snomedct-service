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

import com.google.common.collect.ImmutableList;
import org.testng.annotations.Test;

import static com.google.common.collect.ImmutableList.of;
import static org.t3as.metamap.SemanticTypes.sanitiseSemanticTypes;
import static org.testng.Assert.assertEquals;

public class SemanticTypesTest {

    @Test
    public void testSanitiseSemanticTypes() {
        assertEquals(of(), sanitiseSemanticTypes(null));
        assertEquals(of(), sanitiseSemanticTypes(ImmutableList.<String>of()));
        assertEquals(of(), sanitiseSemanticTypes(of("foobar")));

        assertEquals(of("dsyn"), sanitiseSemanticTypes(of("dsyn")));
        assertEquals(of("dsyn"), sanitiseSemanticTypes(of("dsyn", "foobar")));
        assertEquals(of("dsyn", "fish"), sanitiseSemanticTypes(of("dsyn", "foobar", "fish")));
    }
}
