package org.t3as.snomedct.client;

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

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.t3as.metamap.jaxb.Utterance;
import org.t3as.snomedct.client.jaxb.ObjectMapperProvider;

import javax.ws.rs.core.MediaType;
import java.util.Collection;

/**
 * A simple client implementation that will call the REST web service with some text and return the results.
 */
public class SnomedClient {

    public static final String DEFAULT_BASE_URL = "http://snomedct.t3as.org/snomed-coder-web/";
    private static final String SNOMED_SERVICE_PATH = "rest/v1.0/snomedctCodes";

    private final String url;

    /**
     * Create a new client that calls the existing demo web service - @see DEFAULT_BASE_URL.
     */
    @SuppressWarnings("UnusedDeclaration")
    public SnomedClient() {
        this.url = DEFAULT_BASE_URL;
    }

    /**
     * Configure the client instance with the base URL where the web service to be used lives.
     * @param url the base URL of the web service to be used
     */
    public SnomedClient(final String url) {
        this.url = url;
    }

    /**
     * Call the webservice with some text to analyse for SNOMED CT codes.
     * @param text text to analyse
     * @return object graph containing all the details about the analysis
     */
    public Collection<Utterance> call(final String text) {
        final ClientConfig config = new DefaultClientConfig();
        config.getClasses().add(JacksonJsonProvider.class);
        config.getClasses().add(ObjectMapperProvider.class);
        final Client client = Client.create(config);
        final WebResource service = client.resource(url + SNOMED_SERVICE_PATH);

        final ClientResponse response = service.type(MediaType.APPLICATION_JSON)
                                               .accept(MediaType.APPLICATION_JSON)
                                               .post(ClientResponse.class, new AnalysisRequest(text));
        return response.getEntity(new GenericType<Collection<Utterance>>() {});
    }
}
