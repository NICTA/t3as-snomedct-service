package org.t3as.snomedct.gwt.client.snomed;

/*
 * #%L
 * t3as SNOMED CT GWT UI
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public final class TestData {

    @SuppressWarnings("PublicStaticCollectionField")
    public static final Collection<SnomedConcept> unmodifiableTestData;

    static {
        final Collection<SnomedConcept> c = new ArrayList<>();
        c.add(new SnomedConcept("269363005", "Contusions", false, "6", 6, "ZZ", "6"));
        c.add(new SnomedConcept("41607009", "Renal cell carcinoma", false, "1", 1, "B", "1"));
        c.add(new SnomedConcept("194756002", "Hypertensive disease", false, "5", 5, "AB", "5"));
        c.add(new SnomedConcept("419045004", "Loss of consciousness", false, "7", 7, "AAA", "7"));
        c.add(new SnomedConcept("60022001", "Possible diagnosis", false, "2", 2, "C", "2"));
        c.add(new SnomedConcept("129006008", "Walking", false, "4", 4, "AA", "4"));
        c.add(new SnomedConcept("91251008", "Physiotherapy", false, "3", 3, "H", "3"));
        c.add(new SnomedConcept("8966001", "Left eye structure", false, "0", 0, "A", "0"));
        unmodifiableTestData = Collections.unmodifiableCollection(c);
    }
}
