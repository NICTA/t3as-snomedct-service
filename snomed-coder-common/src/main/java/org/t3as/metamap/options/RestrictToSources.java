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
package org.t3as.metamap.options;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

import java.util.List;

import static com.google.common.base.CharMatcher.JAVA_LETTER_OR_DIGIT;
import static com.google.common.base.CharMatcher.anyOf;

public class RestrictToSources extends Option {

    protected static final String NAME = "restrict_to_sources";
    protected static final String SNOMEDCT_US = "SNOMEDCT_US";
    private static final String USE_METAMAP_DEFAULT_STRING = "[default]";

    private final List<String> sources;

    public RestrictToSources() { sources = ImmutableList.of(SNOMEDCT_US); }

    public RestrictToSources(final List<String> params) { sources = params; }

    @Override
    public String name() { return NAME; }

    @Override
    public String param() { return Joiner.on(',').skipNulls().join(sources); }

    @Override
    public boolean useMetamapDefault() {
        return sources.size() == 1 && USE_METAMAP_DEFAULT_STRING.equals(sources.get(0));
    }

    @Override
    protected Option newInstance(final String param) {
        // sanitize the user input, only keep letters, digits, and any of a small number of approved params
        final String sanitized = JAVA_LETTER_OR_DIGIT.or(anyOf(".,_-[]")).retainFrom(param);
        final List<String> params = Splitter.on(',').trimResults().omitEmptyStrings().splitToList(sanitized);
        return params.isEmpty() ? null : new RestrictToSources(params);
    }
}
