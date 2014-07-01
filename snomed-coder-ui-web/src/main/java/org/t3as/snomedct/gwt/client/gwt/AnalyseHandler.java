/*
 * #%L
 * NICTA t3as SNOMED CT GWT UI
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
package org.t3as.snomedct.gwt.client.gwt;

import com.google.common.base.Joiner;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import org.t3as.snomedct.gwt.client.Messages;
import org.t3as.snomedct.gwt.client.SemanticType;
import org.t3as.snomedct.gwt.client.snomed.SnomedConcept;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class AnalyseHandler implements ClickHandler {

    private final Messages messages = GWT.create(Messages.class);
    private final String webserviceUrl;
    private final TextArea mainTextArea;
    private final Label statusLabel;
    private final List<SnomedConcept> conceptList;
    private final List<SemanticType> types;
    private final Map<String, String> typeCodeToDescription;
    private final GlassLoadingPanel glassPanel;

    public AnalyseHandler(final TextArea mainTextArea, final Label statusLabel,
                          final List<SnomedConcept> conceptList, final List<SemanticType> types,
                          final Map<String, String> typeCodeToDescription) {
        this.mainTextArea = mainTextArea;
        this.statusLabel = statusLabel;
        this.conceptList = conceptList;
        this.types = types;
        this.typeCodeToDescription = typeCodeToDescription;
        this.glassPanel = new GlassLoadingPanel();

        final String hostAndPort = Cookies.getCookie("webserviceHost");
        this.webserviceUrl = (hostAndPort.isEmpty() ? "" : "http://" + hostAndPort)
                             + "/snomed-coder-web/rest/v1.0/snomedctCodes";
    }

    public void onClick(final ClickEvent event) {
        sendTextToServer();
    }

    // send the text from the mainTextArea to the server and accept an async response
    private void sendTextToServer() {
        statusLabel.setText("");
        conceptList.clear();

        // don't do anything if we have no text
        final String text = mainTextArea.getText();
        if (text.length() < 1) {
            statusLabel.setText(messages.pleaseEnterTextLabel());
            return;
        }

        // disable interaction while we wait for the response
        glassPanel.setPositionAndShow();

        // build up the AnalysisRequest JSON object
        // start with any options
        final JSONArray options = new JSONArray();
        setSemanticTypesOption(types, options);
        // defaults
        options.set(options.size(), new JSONString("word_sense_disambiguation"));
        options.set(options.size(), new JSONString("composite_phrases 8"));
        options.set(options.size(), new JSONString("no_derivational_variants"));
        options.set(options.size(), new JSONString("strict_model"));
        options.set(options.size(), new JSONString("ignore_word_order"));
        options.set(options.size(), new JSONString("allow_large_n"));
        options.set(options.size(), new JSONString("restrict_to_sources SNOMEDCT_US"));

        final JSONObject analysisRequest = new JSONObject();
        analysisRequest.put("text", new JSONString(text));
        analysisRequest.put("options", options);

        // send the input to the server
        final RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, webserviceUrl);
        builder.setHeader("Content-Type", MediaType.APPLICATION_JSON);
        builder.setRequestData(analysisRequest.toString());

        // create the async callback
        builder.setCallback(new SnomedRequestCallback(conceptList, statusLabel, glassPanel, typeCodeToDescription));

        // send the request
        try { builder.send(); }
        catch (final RequestException e) {
            statusLabel.setText(messages.problemPerformingAnalysisLabel());
            GWT.log("There was a problem performing the analysis: " + e.getMessage(), e);
            glassPanel.hide();
        }
    }

    private static void setSemanticTypesOption(final Collection<SemanticType> types, final JSONArray options) {
        final Collection<String> selectedTypes = new ArrayList<>();
        for (final SemanticType t : types) {
            if (t.isSelected()) selectedTypes.add(t.getCode());
        }
        options.set(options.size(), new JSONString("restrict_to_sts " + Joiner.on(',').join(selectedTypes)));
    }
}
