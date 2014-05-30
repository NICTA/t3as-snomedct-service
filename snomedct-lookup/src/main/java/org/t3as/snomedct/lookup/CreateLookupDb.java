package org.t3as.snomedct.lookup;

/*
 * #%L
 * NICTA t3as UMLS CUI to SNOMED CT Lookup
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

import org.h2.jdbcx.JdbcConnectionPool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.join;
import static org.t3as.metamap.JaxbLoader.loadResource;

public class CreateLookupDb implements AutoCloseable {

    private static final String INSERT_SQL =
            "insert into snomed (snomedid,cui,termType,mrrank,description) values (?,?,?,?,?)";

    private final File umlsMetaPath;
    private final JdbcConnectionPool connectionPool;
    private final Connection connection;

    public CreateLookupDb(final File dbFile, final File umlsMetaPath) throws SQLException {
        this.umlsMetaPath = umlsMetaPath;
        this.connectionPool = JdbcConnectionPool.create("jdbc:h2:" + dbFile.getAbsoluteFile(), "foo", "bar");
        this.connection = connectionPool.getConnection();
    }

    public int process() throws SQLException, IOException {
        // load and process the MRRANK file
        final Map<String, Integer> rankMap = new HashMap<>();
        try (final BufferedReader mrrank = new BufferedReader(
                new FileReader(umlsMetaPath.getAbsolutePath() + "/MRRANK.RRF"))
        ) {
            int rank = 1;
            for (String line; (line = mrrank.readLine()) != null; ) {
                final String[] p = line.split("\\|", 0);
                if (!p[1].startsWith("SNOMED")) continue;
                rankMap.put(p[2], rank);
                rank++;
            }
        }
        // create the table
        try (final Statement statement = connection.createStatement()) {
            statement.execute(join(loadResource("create-snomed-db.sql"), "\n"));
        }

        // process the UMLS data
        int count = 0;
        try (final PreparedStatement prep = connection.prepareStatement(INSERT_SQL);
             final BufferedReader mrconso =
                     new BufferedReader(new FileReader(umlsMetaPath.getAbsolutePath() + "/MRCONSO.RRF"))
        ) {
            for (String line; (line = mrconso.readLine()) != null; ) {
                final String[] p = line.split("\\|", 0);
                if (!p[11].startsWith("SNOMED")) continue;
                prep.setString(1, p[13]);           // SNOMED CT code
                prep.setString(2, p[0]);            // CUI
                prep.setString(3, p[12]);           // term type
                prep.setInt(4, rankMap.get(p[12])); // term type rank
                prep.setString(5, p[14]);           // description
                prep.execute();
                count++;
            }
        }
        return count;
    }

    @Override
    public void close() throws Exception {
        connection.close();
        connectionPool.dispose();
    }
}
