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
 * it with H2, GWT, or JavaBeans Activation Framework (JAF) (or a
 * modified version of those libraries), containing parts covered by the
 * terms of the H2 License, the GWT Terms, or the Common Development and
 * Distribution License (CDDL) version 1.0 ,the licensors of this Program
 * grant you additional permission to convey the resulting work.
 * #L%
 */
package org.t3as.snomedct.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.t3as.metamap.jaxb.Utterance;
import org.t3as.snomedct.client.jaxb.ObjectMapperProvider;
import org.t3as.snomedct.service.AnalysisRequest;

import javax.ws.rs.core.MediaType;
import java.util.Collection;

/**
 * A simple client implementation that will call the REST web service with some text and return the results. The 'call'
 * method can and should be reused repeatedly, since creating the Jersey Client is a relatively time consuming
 * operation.
 */
public class SnomedClient {

    public static final String DEFAULT_BASE_URL = "http://snomedct.t3as.org/snomed-coder-web/";
    private static final String SNOMED_SERVICE_PATH = "rest/v1.0/snomedctCodes";

    private final WebResource service;

    /**
     * Create a new client that calls the existing demo web service - this will use the default base URL
     * "{@value #DEFAULT_BASE_URL}".
     */
    @SuppressWarnings("UnusedDeclaration")
    public SnomedClient() {
        this(DEFAULT_BASE_URL);
    }

    /**
     * Configure the client instance with the base URL where the web service to be used lives.
     * @param url the base URL of the web service to be used
     */
    public SnomedClient(final String url) {
        final ClientConfig config = new DefaultClientConfig();
        config.getClasses().add(JacksonJsonProvider.class);
        config.getClasses().add(ObjectMapperProvider.class);
        final Client client = Client.create(config);
        service = client.resource(url + SNOMED_SERVICE_PATH);
    }

    /**
     * Call the webservice with some request to analyse for SNOMED CT codes - this method can be used repeatedly on the
     * same SnomedClient instance.
     * @param request request to analyse
     * @return object graph containing all the analysis output
     */
    public Collection<Utterance> call(final AnalysisRequest request) {
        ClientResponse response = null;
        try {
            response = service.type(MediaType.APPLICATION_JSON)
                              .accept(MediaType.APPLICATION_JSON)
                              .post(ClientResponse.class, request);
            return response.getEntity(new GenericType<Collection<Utterance>>() {});
        }
        finally {
            // belt and braces
            if (response != null) {
                //noinspection OverlyBroadCatchBlock
                try { response.close(); }
                catch (final Throwable ignored) { /* do nothing */ }
            }
        }
    }
}
