package org.t3as.metamap;

/*
 * #%L
 * NICTA t3as MetaMap Tagger
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

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.t3as.metamap.options.AllowLargeN;
import org.t3as.metamap.options.CompositePhrases;
import org.t3as.metamap.options.IgnoreWordOrder;
import org.t3as.metamap.options.NoDerivationalVariants;
import org.t3as.metamap.options.Option;
import org.t3as.metamap.options.StrictModel;
import org.t3as.metamap.options.WordSenseDisambiguation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.t3as.metamap.JaxbLoader.loadResource;

public final class MetaMap {

    /**
     * This has to be downloaded from the NLM MetaMap site. Last known URL was:
     * http://metamap.nlm.nih.gov/Docs/SemanticTypes_2013AA.txt
     */
    private static final String SEMANTIC_TYPES_FILE = "SemanticTypes_2013AA.txt";
    private static final String DEFAULT_SEMANTIC_TYPES_FILE = "defaultSemanticTypes.txt";

    @SuppressWarnings("PublicStaticCollectionField")
    public static final Collection<String> DEFAULT_MM_SEMANTIC_TYPES;
    @SuppressWarnings("PublicStaticCollectionField")
    public static final Map<String, String> SEMANTIC_TYPES_CODE_TO_DESCRIPTION;
    @SuppressWarnings("PublicStaticCollectionField")
    public static final Collection<Option> DEFAULT_MM_OPTIONS;

    private final File publicMm;
    private final Collection<String> semanticTypes;

    static {
        final Map<String, String> semanticTypes = new TreeMap<>();
        final Collection<String> defaultSemanticTypes = new ArrayList<>();
        try {
            Collections.addAll(defaultSemanticTypes, loadResource(DEFAULT_SEMANTIC_TYPES_FILE));

            // load all the MetaMap Semantic Types and make a static map out of the code and descriptions
            final String[] semanticTypeLines = loadResource(SEMANTIC_TYPES_FILE);
            for (final String line : semanticTypeLines) {
                final String[] parts = line.split("\\|");
                semanticTypes.put(parts[0], parts[2]);
            }
        }
        catch (final IOException e) {
            System.err.println("Could not load MetaMap Semantic Types: " + e.getMessage());
            e.printStackTrace();
        }

        DEFAULT_MM_SEMANTIC_TYPES = Collections.unmodifiableCollection(defaultSemanticTypes);
        SEMANTIC_TYPES_CODE_TO_DESCRIPTION = Collections.unmodifiableMap(semanticTypes);

        final Collection<Option> o = new ArrayList<>();
        o.add(new WordSenseDisambiguation());
        o.add(new CompositePhrases(8));
        o.add(new NoDerivationalVariants());
        o.add(new StrictModel());
        o.add(new IgnoreWordOrder());
        o.add(new AllowLargeN());
        DEFAULT_MM_OPTIONS = Collections.unmodifiableCollection(o);
    }

    public MetaMap(final File publicMm, final Collection<String> semanticTypes) {
        this.publicMm = publicMm;
        this.semanticTypes = sanitiseSemanticTypes(semanticTypes);
    }

    public boolean process(final File input, final File output, final Option[] opts)
            throws IOException, InterruptedException {

        // put the options together
        final List<String> o = new ArrayList<>();
        o.add(publicMm.getAbsolutePath() + "/bin/metamap13");
        if (opts != null) {
            for (final Option opt : opts) {
                o.add(opt.toMmOptStr());
            }
        }
        o.add("--restrict_to_sources SNOMEDCT_US");
        o.add("--XMLf1");
        o.add("--restrict_to_sts " + StringUtils.join(semanticTypes, ","));
        o.add(input.getAbsolutePath());
        o.add(output.getAbsolutePath());

        // run MetaMap to produce XML output
        final ProcessBuilder pb = new ProcessBuilder();
        pb.command(o.toArray(new String[o.size()]));
        pb.directory(publicMm);
        pb.inheritIO();
        final long start = System.currentTimeMillis();
        final Process mm = pb.start();
        final int exitCode = mm.waitFor();
        final long end = System.currentTimeMillis();
        System.out.printf("MetaMap processing finished in %,d milliseconds.\n", end - start);
        return exitCode == 0;
    }

    /**
     * Takes a Unicode string and tries to decompose non-7bit-ascii (Unicode Basic Latin) characters into 7bit ascii.
     * For example, the string 'âåäöốở' is turned into 'aaaooo'.
     * Note that it doesn't always succeed for some of the much more complicated characters (e.g. 'µ').
     * Occasionally some complicated characters end up as two characters when the ASCIIFoldingFilter is used...
     * Perhaps we want to adopt this library:
     * http://www.ippatsuman.com/projects/junidecode/
     */
    public static String decomposeToAscii(final String s) {
        /* pure java version, doesn't work all the time:
        String normalized = Normalizer.normalize(s, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        */

        // this works on more cases
        final char[] input = new char[s.length()];
        s.getChars(0, s.length(), input, 0);
        final char[] output = new char[input.length * 4];
        final int numChars = ASCIIFoldingFilter.foldToASCII(input, 0, output, 0, input.length);

        // now remove anything not in the printable US-ASCII range, but keep newlines
        final StringBuilder sb = new StringBuilder(numChars);
        for (int i = 0; i < numChars; i++) {
            final char c = output[i];
            // printable US-ASCII is from 32 to 126
            if ((32 <= c && c <= 126) || '\n' == c) sb.append(c);
        }

        return sb.toString();
    }

    /*package-private*/
    static Collection<String> sanitiseSemanticTypes(final Collection<String> semanticTypes) {
        if (semanticTypes == null) return DEFAULT_MM_SEMANTIC_TYPES;

        // check that each of the given types are in the map we have, otherwise throw it away
        final Collection<String> types = new ArrayList<>(semanticTypes.size());
        for (final String t : semanticTypes) {
            if (SEMANTIC_TYPES_CODE_TO_DESCRIPTION.containsKey(t)) types.add(t);
        }

        return types.size() > 0 ? types : DEFAULT_MM_SEMANTIC_TYPES;
    }
}
