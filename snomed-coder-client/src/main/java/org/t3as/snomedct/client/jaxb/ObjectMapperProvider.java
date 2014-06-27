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
package org.t3as.snomedct.client.jaxb;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * We need to register the a JaxbAnnotationIntrospector with the Jackson ObjectMapper so that we can deserialize
 * JSON using the snomed-coder-common JAXB annotations.
 */
@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        final ObjectMapper om = new ObjectMapper();
        om.setDeserializationConfig(
                om.getDeserializationConfig().withAnnotationIntrospector(new JaxbAnnotationIntrospector()));
        OBJECT_MAPPER = om;
    }

    @Override
    public ObjectMapper getContext(final Class<?> type) {
        return OBJECT_MAPPER;
    }
}
