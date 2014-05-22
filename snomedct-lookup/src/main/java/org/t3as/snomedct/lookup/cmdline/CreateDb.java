package org.t3as.snomedct.lookup.cmdline;

/*
 * #%L
 * UMLS CUI to SNOMED CT Lookup
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
import org.t3as.snomedct.lookup.CreateLookupDb;

import java.io.File;

public final class CreateDb {

    public static void main(final String[] args) throws Exception {
        final Options opts = new Options();
        JCommander jc = null;
        try {
            jc = new JCommander(opts, args);
        }
        catch (final Exception e) {
            System.err.println("Could not parse the options: " + e.getMessage());
            System.exit(1);
        }
        if (opts.showUsage) {
            jc.usage();
            System.exit(0);
        }

        System.out.println("Starting database generation...");
        final long start = System.currentTimeMillis();
        try (final CreateLookupDb createLookupDb = new CreateLookupDb(opts.dbFile, opts.umlsMetaPath)) {
            final int count = createLookupDb.process();
            final long end = System.currentTimeMillis();
            System.out.printf("Inserted %,d SNOMED CT concepts in %,d milliseconds.\n", count, end - start);
        }
    }

    private static class Options {
        @Parameter(help = true, names = {"-h", "--help"}, description = "Show this help message.")
        boolean showUsage = false;

        @Parameter(names = "-dbFile", description = "The file prefix to save the SNOMED CT lookup database in.")
        File dbFile = new File("/opt/snomed-coder-web/data/snomedct");

        @Parameter(names = "-umlsMeta", description = "The path to the UMLS META directory.")
        File umlsMetaPath = new File("/opt/snomed-coder-web/umls/2013AB/META");
    }
}
