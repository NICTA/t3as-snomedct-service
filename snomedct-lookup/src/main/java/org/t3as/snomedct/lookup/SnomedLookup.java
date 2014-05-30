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
import org.t3as.metamap.jaxb.Candidate;
import org.t3as.metamap.jaxb.MMO;
import org.t3as.metamap.jaxb.MMOs;
import org.t3as.metamap.jaxb.Mapping;
import org.t3as.metamap.jaxb.Phrase;
import org.t3as.metamap.jaxb.SemType;
import org.t3as.metamap.jaxb.Utterance;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public final class SnomedLookup implements Closeable {

    private static final String SELECT_SQL =
            "select top 1 snomedId, termType from snomed " + // only return 1 result
            "where cui = ? and description = ? " +   // unfortunately we must filter on description
            "order by mrrank, snomedId desc";        // order first by rank, then by highest SNOMED code

    private static final String FROM_SNOMEDID_SELECT_SQL =
            "select cui, description, termType from snomed " +
            "where snomedid = ?";

    private final JdbcConnectionPool connectionPool;
    private final Connection conn;
    private final PreparedStatement fromCuiPrepStmt;
    private final PreparedStatement fromSnomedIdPrepStmt;

    public SnomedLookup(final File dbFile) throws SQLException {
        // open the database in readonly mode, and fail if it does not exist
        connectionPool = JdbcConnectionPool.create(
                "jdbc:h2:" + dbFile.getAbsoluteFile() + ";IFEXISTS=TRUE;ACCESS_MODE_DATA=r", "foo", "bar");
        conn = connectionPool.getConnection();
        fromCuiPrepStmt = conn.prepareStatement(SELECT_SQL);
        fromSnomedIdPrepStmt = conn.prepareStatement(FROM_SNOMEDID_SELECT_SQL);
    }

    /**
     * Tries to look up each Mapping Candidate in the SNOMED CT db to add more data.
     * @return the number of Candidates that were enriched
     */
    public int enrichXml(final MMOs root) throws SQLException {
        int count = 0;
        // TODO: take out the print statements
        for (final MMO mmo : root.getMMO()) {
            for (final Utterance utterance : mmo.getUtterances().getUtterance()) {
                for (final Phrase phrase : utterance.getPhrases().getPhrase()) {
                    System.out.printf("Phrase: %s\n", phrase.getPhraseText());
                    for (final Mapping mapping : phrase.getMappings().getMapping()) {
                        System.out.printf("Score: %s\n", mapping.getMappingScore());
                        for (final Candidate candidate : mapping.getMappingCandidates().getCandidate()) {
                            final Collection<String> semTypes = new ArrayList<>();
                            for (final SemType st : candidate.getSemTypes().getSemType()) {
                                semTypes.add(st.getvalue());
                            }

                            // the actual line of work
                            count += addSnomedId(candidate) ? 1 : 0;

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
        return count;
    }

    /**
     * Tries to find this candidate in the SNOMED CT database, and if found adds the SNOMED id and term type to the
     * instance.
     */
    public boolean addSnomedId(final Candidate candidate) throws SQLException {
        final SnomedTerm result = findFromCuiAndDesc(candidate.getCandidateCUI(), candidate.getCandidatePreferred());
        if (result != null) {
            candidate.setSnomedId(result.snomedId);
            candidate.setTermType(result.termType);
            return true;
        }
        else {
            // TODO: log this somewhere?
            System.err.printf("WARNING! Could not find: %s: '%s'\n",
                              candidate.getCandidateCUI(), candidate.getCandidatePreferred());
            return false;
        }
    }

    public SnomedTerm findFromCuiAndDesc(final String cui, final String description) throws SQLException {
        //noinspection JpaQueryApiInspection
        fromCuiPrepStmt.setString(1, cui);
        //noinspection JpaQueryApiInspection
        fromCuiPrepStmt.setString(2, description);
        final ResultSet resultSet = fromCuiPrepStmt.executeQuery();
        SnomedTerm t = null;
        if (resultSet.first()) {
            t = new SnomedTerm(cui, resultSet.getString("snomedId"), resultSet.getString("termType"),
                               description);
        }
        resultSet.close();
        return t;
    }

    public SnomedTerm findFromSnomedId(final String snomedId) throws SQLException {
        //noinspection JpaQueryApiInspection
        fromSnomedIdPrepStmt.setString(1, snomedId);
        final ResultSet resultSet = fromSnomedIdPrepStmt.executeQuery();
        SnomedTerm t = null;
        if (resultSet.first()) {
            t = new SnomedTerm(resultSet.getString("cui"), snomedId, resultSet.getString("termType"),
                               resultSet.getString("description"));
        }
        resultSet.close();
        return t;
    }

    @Override
    public void close() throws IOException {
        try {
            fromCuiPrepStmt.close();
            fromSnomedIdPrepStmt.close();
            conn.close();
            connectionPool.dispose();
        }
        catch (final SQLException e) {
            throw new IOException("Could not close SQL connection: " + e.getMessage(), e);
        }
    }
}
