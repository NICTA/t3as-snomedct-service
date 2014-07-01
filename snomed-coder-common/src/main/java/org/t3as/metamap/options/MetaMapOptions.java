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
 * it with H2, GWT, or JavaBeans Activation Framework (JAF) (or a
 * modified version of those libraries), containing parts covered by the
 * terms of the H2 License, the GWT Terms, or the Common Development and
 * Distribution License (CDDL) version 1.0 ,the licensors of this Program
 * grant you additional permission to convey the resulting work.
 * #L%
 */
package org.t3as.metamap.options;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.t3as.metamap.SemanticTypes;

import java.util.List;
import java.util.regex.Pattern;

import static com.google.common.base.CharMatcher.JAVA_LETTER_OR_DIGIT;
import static com.google.common.base.CharMatcher.anyOf;
import static org.t3as.metamap.options.RestrictToSources.SNOMEDCT_US;

public abstract class MetaMapOptions {

    @SuppressWarnings("PublicStaticCollectionField")
    public static final ImmutableCollection<Option> DEFAULT_MM_OPTIONS;
    private static final ImmutableMap<String, Option> OPTS;
    private static final Pattern SPACE = Pattern.compile(" ");

    static {
        // PUT NEW OPTIONS IN THIS LIST
        OPTS = ImmutableMap.<String, Option>builder()
                           .put(WordSenseDisambiguation.NAME, new WordSenseDisambiguation())
                           .put(CompositePhrases.NAME, new CompositePhrases())
                           .put(NoDerivationalVariants.NAME, new NoDerivationalVariants())
                           .put(StrictModel.NAME, new StrictModel())
                           .put(IgnoreWordOrder.NAME, new IgnoreWordOrder())
                           .put(AllowLargeN.NAME, new AllowLargeN())
                           .put(IgnoreStopPhrases.NAME, new IgnoreStopPhrases())
                           .put(AllAcrosAbbrs.NAME, new AllAcrosAbbrs())
                           .put(RestrictToSources.NAME, new RestrictToSources())
                           .put(RestrictToSts.NAME, new RestrictToSts())
                           .build();

        DEFAULT_MM_OPTIONS = ImmutableList.of(
                new WordSenseDisambiguation(),
                new CompositePhrases(8),
                new NoDerivationalVariants(),
                new StrictModel(),
                new IgnoreWordOrder(),
                new AllowLargeN(),
                new RestrictToSources(ImmutableList.of(SNOMEDCT_US)),
                new RestrictToSts(SemanticTypes.DEFAULT_MM_SEMANTIC_TYPES));
    }

    private MetaMapOptions() {}

    /**
     * Parse an option string (without the leading double hyphens) into an option to pass to MetaMap,
     * e.g. "word_sense_disambiguation".
     */
    @SuppressWarnings("ReturnOfNull")
    public static Option strToOpt(final String optStr) {
        final String[] parts = SPACE.split(optStr, 2);
        final String name = parts[0];
        final String param = 1 < parts.length ? parts[1] : null;

        final Option opt = OPTS.get(name);
        return opt == null ? null : opt.newInstance(param);
    }

    /*package-private*/
    static List<String> sanitiseAndSplit(final String param) {
        // sanitize the user input, only keep letters, digits, and any of a small number of approved params
        final String sanitized = JAVA_LETTER_OR_DIGIT.or(anyOf(".,_-[]")).retainFrom(param);
        return Splitter.on(',').trimResults().omitEmptyStrings().splitToList(sanitized);
    }
}
