package org.t3as.snomedct.service;

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
 * it with H2, GWT, JUnit, or JavaBeans Activation Framework (JAF) (or a
 * modified version of those libraries), containing parts covered by the
 * terms of the H2 License, the GWT Terms, the Common Public License
 * Version 1.0, or the Common Development and Distribution License (CDDL)
 * version 1.0 ,the licensors of this Program grant you additional
 * permission to convey the resulting work.
 * #L%
 */

import org.t3as.metamap.MetaMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Path("/v1.0/semanticTypes")
public class SemanticTypesService {

    private static final Collection<SemanticType> SEMANTIC_TYPES;

    static {
        final Collection<SemanticType> types = new ArrayList<>(MetaMap.SEMANTIC_TYPES_CODE_TO_DESCRIPTION.size());
        for (final Map.Entry<String, String> entry : MetaMap.SEMANTIC_TYPES_CODE_TO_DESCRIPTION.entrySet()) {
            types.add(new SemanticType(entry.getKey(), entry.getValue(),
                                       MetaMap.DEFAULT_MM_SEMANTIC_TYPES.contains(entry.getKey())));
        }
        SEMANTIC_TYPES = Collections.unmodifiableCollection(types);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public static Collection<SemanticType> semanticTypes() throws IOException {
        return SEMANTIC_TYPES;
    }

    @SuppressWarnings("UnusedDeclaration")
    private static class SemanticType {
        final String code;
        final String description;
        final boolean isOnByDefault;

        public SemanticType(final String code, final String description, final boolean isDefault) {
            this.code = code;
            this.description = description;
            this.isOnByDefault = isDefault;
        }

        public boolean isOnByDefault() { return isOnByDefault; }

        public String getCode() { return code; }

        public String getDescription() { return description; }
    }
}
