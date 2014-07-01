/*
 * #%L
 * NICTA t3as SNOMED CT web service
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
package org.t3as.snomedct.service;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.sun.jersey.api.Responses;
import org.t3as.metamap.JaxbLoader;
import org.t3as.metamap.MetaMap;
import org.t3as.metamap.jaxb.MMO;
import org.t3as.metamap.jaxb.MMOs;
import org.t3as.metamap.jaxb.Phrase;
import org.t3as.metamap.jaxb.Utterance;
import org.t3as.metamap.options.MetaMapOptions;
import org.t3as.metamap.options.Option;
import org.t3as.metamap.options.RestrictToSources;
import org.t3as.snomedct.lookup.SnomedLookup;
import org.xml.sax.SAXException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

@Path("/v1.0/snomedctCodes")
public class SnomedCoderService {

    private static final File PUBLIC_MM_DIR = new File(
            System.getProperty("publicMmDir", "/opt/snomed-coder-web/metamap/public_mm"));
    private static final File DB_PATH = new File(
            System.getProperty("snomedDbPath", "/opt/snomed-coder-web/data/snomedct"));
    private static final long MAX_DATA_BYTES = 10 * 1024; // limit to 10kb for now

    /**
     * Return some docs about how to call this webservice
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public InputStream doc() throws IOException {
        //noinspection ConstantConditions
        return getClass().getClassLoader().getResource("SnomedCoderService_help.txt").openStream();
    }

    /**
     * Accepts application/x-www-form-urlencoded text to analyse.
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public static Collection<Utterance> mapText(final String text)
            throws IOException, InterruptedException, JAXBException, SQLException, ParserConfigurationException,
                   SAXException {
        return mapTextWithOptions(new AnalysisRequest(text));
    }

    /**
     * Accepts a JSON object with text and possible options for the analysis.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
    public static Collection<Utterance> mapTextWithOptions(final AnalysisRequest r)
            throws IOException, InterruptedException, JAXBException, SQLException, ParserConfigurationException,
                   SAXException {

        System.out.println(r);

        // metamap options
        final Collection<Option> opts = new ArrayList<>();
        final Set<String> optionNames = new HashSet<>();
        for (final String o : r.getOptions()) {
            final Option opt = MetaMapOptions.strToOpt(o);
            if (opt != null) {
                opts.add(opt);
                optionNames.add(opt.name());
            }
        }
        // always make sure we have a restrict_to_sources option
        if (!optionNames.contains(new RestrictToSources().name())) opts.add(new RestrictToSources());

        // tmp files for metamap in/out
        final File infile = File.createTempFile("metamap-input-", ".txt");
        final File outfile = File.createTempFile("metamap-output-", ".xml");
        final String s = r.getText() + (r.getText().endsWith("\n") ? "" : "\n");
        final String ascii = MetaMap.decomposeToAscii(URLDecoder.decode(s, "UTF-8"));
        Files.write(ascii, infile, Charsets.UTF_8);
        // we don't want too much data for a free service
        if (infile.length() > MAX_DATA_BYTES) {
            throw new WebApplicationException(
                    Response.status(Responses.NOT_ACCEPTABLE)
                            .entity("Too much data, currently limited to " + MAX_DATA_BYTES + " bytes.")
                            .type("text/plain").build()
            );
        }

        // process the data with MetaMap
        final MetaMap metaMap = new MetaMap(PUBLIC_MM_DIR, opts);
        if (!metaMap.process(infile, outfile)) {
            throw new WebApplicationException(Response.status(INTERNAL_SERVER_ERROR)
                                                      .entity("Processing failed, aborting.")
                                                      .type("text/plain").build());
        }

        // look up the SNOMED codes from the UMLS CUI code/description combinations returned by MetaMap
        final MMOs root = JaxbLoader.loadXml(outfile);
        try (final SnomedLookup snomedLookup = new SnomedLookup(DB_PATH)) {
            snomedLookup.enrichXml(root);
        }

        //noinspection ResultOfMethodCallIgnored
        infile.delete();
        //noinspection ResultOfMethodCallIgnored
        outfile.delete();

        return destructiveFilter(root);
    }

    /**
     * Beware! Destructive filtering of things we are not interested in in the JAXB data structure.
     */
    private static Collection<Utterance> destructiveFilter(final MMOs root) {
        final Collection<Utterance> utterances = new ArrayList<>();
        for (final MMO mmo : root.getMMO()) {
            for (final Utterance utterance : mmo.getUtterances().getUtterance()) {

                // clear candidates to save heaps of bytes
                for (final Phrase phrase : utterance.getPhrases().getPhrase()) {
                    phrase.setCandidates(null);
                }
                utterances.add(utterance);
            }
        }
        return utterances;
    }
}
