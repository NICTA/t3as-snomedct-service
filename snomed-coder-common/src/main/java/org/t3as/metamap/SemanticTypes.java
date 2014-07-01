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
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.Resources.getResource;
import static com.google.common.io.Resources.readLines;

public final class SemanticTypes {

    /**
     * This has to be downloaded from the NLM MetaMap site. Last known URL was:
     * http://metamap.nlm.nih.gov/Docs/SemanticTypes_2013AA.txt
     */
    private static final String SEMANTIC_TYPES_FILE = "SemanticTypes_2013AA.txt";
    private static final String DEFAULT_SEMANTIC_TYPES_FILE = "defaultSemanticTypes.txt";

    @SuppressWarnings("PublicStaticCollectionField")
    public static final ImmutableList<String> DEFAULT_MM_SEMANTIC_TYPES;
    @SuppressWarnings("PublicStaticCollectionField")
    public static final ImmutableMap<String, String> SEMANTIC_TYPES_CODE_TO_DESCRIPTION;

    static {
        final Map<String, String> semanticTypes = new HashMap<>();
        final Collection<String> defaultSemanticTypes = new ArrayList<>();
        try {
            defaultSemanticTypes.addAll(readLines(getResource(DEFAULT_SEMANTIC_TYPES_FILE), UTF_8));

            // load all the MetaMap Semantic Types and make a static map out of the code and descriptions
            final Collection<String> semanticTypeLines = readLines(getResource(SEMANTIC_TYPES_FILE), UTF_8);
            final Pattern PIPE = Pattern.compile("\\|");
            for (final String line : semanticTypeLines) {
                final String[] parts = PIPE.split(line);
                semanticTypes.put(parts[0], parts[2]);
            }
        }
        catch (final IOException e) {
            System.err.println("Could not load MetaMap Semantic Types: " + e.getMessage());
            e.printStackTrace();
        }

        DEFAULT_MM_SEMANTIC_TYPES = ImmutableList.copyOf(defaultSemanticTypes);
        SEMANTIC_TYPES_CODE_TO_DESCRIPTION = ImmutableSortedMap.copyOf(semanticTypes);
    }

    private SemanticTypes() {}

    /**
     * Sanitise semantic types from user input - if there is a single value '[all]' then an empty Collection is
     * returned.
     * @param semanticTypes the semantic types to be sanitised
     * @return a non-null but possibly empty Collection of semantic types - if there is a single value '[all]' then
     * an empty Collection is returned.
     */
    /*package-private*/ static Collection<String> sanitiseSemanticTypes(final Collection<String> semanticTypes) {
        if (semanticTypes == null) return DEFAULT_MM_SEMANTIC_TYPES;

        Collection<String> types;
        if (semanticTypes.size() == 1 && "[all]".equals(semanticTypes.iterator().next())) {
            types = Collections.emptyList();
        }
        else {
            // check that each of the given types are in the map we have, otherwise throw it away
            types = new ArrayList<>(semanticTypes.size());
            for (final String t : semanticTypes) {
                if (SEMANTIC_TYPES_CODE_TO_DESCRIPTION.containsKey(t)) types.add(t);
            }
            // if no valid types then return the default ones
            types = types.isEmpty() ? DEFAULT_MM_SEMANTIC_TYPES : types;
        }

        return types;
    }
}
