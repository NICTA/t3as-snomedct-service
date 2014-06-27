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
package org.t3as.metamap.options;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class Options {

    @SuppressWarnings("PublicStaticCollectionField")
    public static final Collection<Option> DEFAULT_MM_OPTIONS;

    private static final Map<String, Option> OPTS;

    static {
        final Map<String, Option> m = new HashMap<>();

        // PUT NEW OPTIONS IN THIS LIST
        m.put(WordSenseDisambiguation.NAME, new WordSenseDisambiguation());
        m.put(CompositePhrases.NAME, new CompositePhrases());
        m.put(NoDerivationalVariants.NAME, new NoDerivationalVariants());
        m.put(StrictModel.NAME, new StrictModel());
        m.put(IgnoreWordOrder.NAME, new IgnoreWordOrder());
        m.put(AllowLargeN.NAME, new AllowLargeN());
        m.put(IgnoreStopPhrases.NAME, new IgnoreStopPhrases());
        m.put(AllAcrosAbbrs.NAME, new AllAcrosAbbrs());

        OPTS = Collections.unmodifiableMap(m);

        final Collection<Option> o = new ArrayList<>();
        o.add(new WordSenseDisambiguation());
        o.add(new CompositePhrases(8));
        o.add(new NoDerivationalVariants());
        o.add(new StrictModel());
        o.add(new IgnoreWordOrder());
        o.add(new AllowLargeN());
        DEFAULT_MM_OPTIONS = Collections.unmodifiableCollection(o);
    }

    private Options() {}

    /**
     * Parse an option string (without the leading double hyphens) into an option to pass to MetaMap,
     * e.g. "word_sense_disambiguation".
     */
    public static Option strToOpt(final String optStr) {
        final String[] parts = optStr.split(" ", 2);
        final String name = parts[0];
        final String param = 1 < parts.length ? parts[1] : null;

        final Option opt = OPTS.get(name);
        return opt == null ? null : opt.newInstance(param);
    }
}
