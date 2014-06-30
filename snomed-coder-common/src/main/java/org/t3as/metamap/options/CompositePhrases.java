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

public class CompositePhrases extends Option {

    private static final int MIN_COMPOSITE_WORDS = 1;
    private static final int MAX_COMPOSITE_WORDS = 8;

    protected static final String NAME = "composite_phrases";
    private final int numPhrases;

    protected CompositePhrases() { numPhrases = 4; }

    public CompositePhrases(final int numPhrases) { this.numPhrases = numPhrases; }

    @Override
    public String name() { return NAME; }

    @Override
    public String param() { return Integer.toString(numPhrases); }

    @Override
    protected Option newInstance(final String param) {
        try {
            final int n = Integer.parseInt(param);
            if (n < MIN_COMPOSITE_WORDS || n > MAX_COMPOSITE_WORDS) return null;
            return new CompositePhrases(n);
        }
        catch (final Throwable ignore) {}
        return null;
    }
}
