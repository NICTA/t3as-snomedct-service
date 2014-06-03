package org.t3as.snomedct.client.cmdline;

/*
 * #%L
 * NICTA t3as SNOMED CT service REST client
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

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.t3as.metamap.jaxb.Candidate;
import org.t3as.metamap.jaxb.Mapping;
import org.t3as.metamap.jaxb.Phrase;
import org.t3as.metamap.jaxb.SemType;
import org.t3as.metamap.jaxb.Utterance;
import org.t3as.snomedct.client.SnomedClient;

import java.util.ArrayList;
import java.util.Collection;

public class Main {

    public static void main(final String[] args) throws Exception {
        final Options opts = new Options();
        JCommander jc = null;
        try { jc = new JCommander(opts, args); }
        catch (final Exception e) {
            System.err.println("Could not parse the options: " + e.getMessage());
            System.exit(1);
        }
        if (opts.showUsage) {
            jc.usage();
            System.exit(0);
        }

        final SnomedClient c = new SnomedClient(opts.url);
        final Collection<Utterance> utterances = c.call(opts.text);

        // just print out the info we are interested in
        for (final Utterance u : utterances) {
            for (final Phrase phrase : u.getPhrases().getPhrase()) {
                System.out.printf("Phrase: %s\n", phrase.getPhraseText());
                for (final Mapping mapping : phrase.getMappings().getMapping()) {
                    System.out.printf("Score: %s\n", mapping.getMappingScore());
                    for (final Candidate candidate : mapping.getMappingCandidates().getCandidate()) {
                        final Collection<String> semTypes = new ArrayList<>();
                        for (final SemType st : candidate.getSemTypes().getSemType()) {
                            semTypes.add(st.getvalue());
                        }

                        System.out.printf("  %-5s %-9s %s %s %s\n",
                                          candidate.getCandidateScore(),
                                          candidate.getCandidateCUI(),
                                          candidate.getSnomedId(),
                                          candidate.getCandidatePreferred(),
                                          semTypes);
                    }
                }
                System.out.println();
            }
        }
    }

    private static class Options {
        @Parameter(help = true, names = {"-h", "--help"}, description = "Show this help message.")
        boolean showUsage = false;

        @Parameter(names = "-url", description = "The base URL of the SNOMED web service to access.")
        String url = SnomedClient.DEFAULT_BASE_URL;

        @Parameter(names = "-text", required = true, description = "The text to analyse for SNOMED CT codes.")
        String text;
    }
}
