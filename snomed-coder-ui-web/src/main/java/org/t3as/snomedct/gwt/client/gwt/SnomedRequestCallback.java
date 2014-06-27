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
 * it with H2, GWT, JUnit, or JavaBeans Activation Framework (JAF) (or a
 * modified version of those libraries), containing parts covered by the
 * terms of the H2 License, the GWT Terms, the Common Public License
 * Version 1.0, or the Common Development and Distribution License (CDDL)
 * version 1.0 ,the licensors of this Program grant you additional
 * permission to convey the resulting work.
 * #L%
 */
package org.t3as.snomedct.gwt.client.gwt;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.Label;
import org.t3as.snomedct.gwt.client.Messages;
import org.t3as.snomedct.gwt.client.jaxbJs.Candidate;
import org.t3as.snomedct.gwt.client.jaxbJs.Mapping;
import org.t3as.snomedct.gwt.client.jaxbJs.Phrase;
import org.t3as.snomedct.gwt.client.jaxbJs.SemType;
import org.t3as.snomedct.gwt.client.jaxbJs.Utterance;
import org.t3as.snomedct.gwt.client.snomed.SnomedConcept;

import java.util.List;
import java.util.Map;

public class SnomedRequestCallback implements RequestCallback {

    private final Messages messages = GWT.create(Messages.class);
    private final List<SnomedConcept> conceptList;
    private final Label statusLabel;
    private final GlassLoadingPanel glassPanel;
    private final Map<String, String> typeCodeToDescription;

    public SnomedRequestCallback(final List<SnomedConcept> conceptList, final Label statusLabel,
                                 final GlassLoadingPanel glassPanel, final Map<String, String> typeCodeToDescription) {
        this.conceptList = conceptList;
        this.statusLabel = statusLabel;
        this.glassPanel = glassPanel;
        this.typeCodeToDescription = typeCodeToDescription;
    }

    public void onError(final Request request, final Throwable e) {
        statusLabel.setText(messages.problemPerformingAnalysisLabel());
        GWT.log("There was a problem performing the analysis: " + e.getMessage(), e);
        glassPanel.hide();
    }

    public void onResponseReceived(final Request request, final Response response) {
        try {
            GWT.log("StatusCode: " + response.getStatusCode() + " " + response.getStatusText());
            statusLabel.setText(messages.parsingCodesLabel());
            String mappingGroup = null;
            final JsArray<Utterance> utterances = JsonUtils.safeEval(response.getText());
            for (int i = 0; i < utterances.length(); i++) {
                final Utterance utterance = utterances.get(i);
                final JsArray<Phrase> phrases = utterance.getPhrases().getPhraseArray();
                for (int j = 0; j < phrases.length(); j++) {
                    final Phrase phrase = phrases.get(j);
                    final JsArray<Mapping> mappings = phrase.getMappings().getMappingArray();
                    for (int k = 0; k < mappings.length(); k++) {
                        final Mapping mapping = mappings.get(k);
                        mappingGroup = nextMappingGroup(mappingGroup);
                        final JsArray<Candidate> candidates = mapping.getCandidateArray();
                        for (int l = 0; l < candidates.length(); l++) {
                            final Candidate candidate = candidates.get(l);
                            // just get the first semantic type for display
                            final SemType semType = candidate.getSemTypeArray().get(0);
                            conceptList.add(new SnomedConcept(
                                    candidate.getSnomedId(),
                                    candidate.getCandidatePreferred(),
                                    candidate.isNegated(),
                                    phrase.getPhraseText(),
                                    candidate.getCandidateScore(),
                                    mappingGroup,
                                    typeCodeToDescription.get(semType.getType())
                            ));
                        }
                    }
                }
            }
            statusLabel.setText(messages.codesFoundLabel(Integer.toString(conceptList.size())));
        }
        catch (final Exception e) {
            statusLabel.setText(messages.problemShowingResults());
            GWT.log("There was a problem showing the results: " + e.getMessage(), e);
        }

        glassPanel.hide();
    }

    /** Iterates A..Z, then AA..AZ to BA..BZ etc. */
    /*package-private*/
    static String nextMappingGroup(final String mappingGroup) {
        if (mappingGroup == null || "".equals(mappingGroup)) return "A";

        final char[] cs = mappingGroup.toCharArray();
        boolean incrementFurther = true;

        // step through the array backwards
        for (int i = cs.length - 1; i >= 0; i--) {
            // if we should no longer increment then break out of the loop
            if (!incrementFurther) break;

            // continue incrementing backwards through the array as long as we are finding Z's
            if (++cs[i] > 'Z') cs[i] = 'A';
            else incrementFurther = false;
        }
        final String result = new String(cs);
        // if the first char of the array was a Z then we have a 'carry' operation and need to add another A at the end
        if (incrementFurther) return result + "A";
        else return result;
    }
}
