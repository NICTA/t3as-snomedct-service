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
import org.t3as.metamap.options.Option;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class MetaMap {

    private final File publicMm;
    private final Collection<String> semanticTypes;

    public MetaMap(final File publicMm, final Collection<String> semanticTypes) {
        this.publicMm = publicMm;
        this.semanticTypes = SemanticTypes.sanitiseSemanticTypes(semanticTypes);
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
        // if we get nothing then don't restrict, i.e. run with all semantic types
        if (semanticTypes != null && !semanticTypes.isEmpty()) {
            o.add("--restrict_to_sts " + StringUtils.join(semanticTypes, ","));
        }
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
}
