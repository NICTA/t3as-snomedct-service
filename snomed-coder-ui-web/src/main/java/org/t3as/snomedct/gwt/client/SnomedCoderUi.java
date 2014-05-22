package org.t3as.snomedct.gwt.client;

/*
 * #%L
 * t3as SNOMED CT GWT UI
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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RowCountChangeEvent;
import org.t3as.snomedct.gwt.client.gwt.AnalyseHandler;
import org.t3as.snomedct.gwt.client.gwt.ConfigureHandler;
import org.t3as.snomedct.gwt.client.snomed.MappingGroupComparator;
import org.t3as.snomedct.gwt.client.snomed.PhraseComparator;
import org.t3as.snomedct.gwt.client.snomed.SnomedCodeComparator;
import org.t3as.snomedct.gwt.client.snomed.SnomedConcept;
import org.t3as.snomedct.gwt.client.snomed.SnomedDescriptionComparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SnomedCoderUi implements EntryPoint {

    private final Messages messages = GWT.create(Messages.class);

    public void onModuleLoad() {
        // create the widgets
        final Button analyseButton = new Button(messages.sendButton());
        final Button configureButton = new Button(messages.configureButton());
        final TextArea mainTextArea = new TextArea();
        mainTextArea.setCharacterWidth(80);
        mainTextArea.setVisibleLines(15);
        final Label errorLabel = new Label();
        final CellTable<SnomedConcept> resultsTable = new CellTable<>();

        // add them to the root panel
        RootPanel.get("mainTextArea").add(mainTextArea);
        RootPanel.get("analyseButton").add(analyseButton);
        RootPanel.get("configureButton").add(configureButton);
        RootPanel.get("status").add(errorLabel);
        RootPanel.get("snomedCodes").add(resultsTable);

        // set the focus to the text area
        mainTextArea.setFocus(true);

        // initialise the SNOMED code results table
        final List<SnomedConcept> conceptList = configSnomedTable(resultsTable);

        // add the handlers
        final List<SemanticType> types = new ArrayList<>();
        final Map<String, String> typeCodeToDescription = new HashMap<>();
        analyseButton.addClickHandler(
                new AnalyseHandler(mainTextArea, errorLabel, conceptList, types, typeCodeToDescription));
        configureButton.addClickHandler(new ConfigureHandler(types, typeCodeToDescription));
    }

    private List<SnomedConcept> configSnomedTable(final CellTable<SnomedConcept> table) {
        // make sure we display all results - no paging
        table.addRowCountChangeHandler(new RowCountChangeEvent.Handler() {
            @Override
            public void onRowCountChange(final RowCountChangeEvent event) {
                table.setVisibleRange(new Range(0, event.getNewRowCount()));
            }
        });

        // setup the columns
        final TextColumn<SnomedConcept> codeCol = new TextColumn<SnomedConcept>() {
            @Override
            public String getValue(final SnomedConcept concept) {
                return concept.snomedId;
            }
        };
        codeCol.setSortable(true);
        final TextColumn<SnomedConcept> descriptiontCol = new TextColumn<SnomedConcept>() {
            @Override
            public String getValue(final SnomedConcept concept) {
                return concept.candidatePreferred;
            }
        };
        descriptiontCol.setSortable(true);
        final TextColumn<SnomedConcept> negatedCol = new TextColumn<SnomedConcept>() {
            @Override
            public String getValue(final SnomedConcept concept) {
                return concept.negated ? messages.negatedHeader() : "";
            }
        };
        final TextColumn<SnomedConcept> phraseCol = new TextColumn<SnomedConcept>() {
            @Override
            public String getValue(final SnomedConcept concept) {
                return concept.phraseText;
            }
        };
        phraseCol.setSortable(true);
        final TextColumn<SnomedConcept> scoreCol = new TextColumn<SnomedConcept>() {
            @Override
            public String getValue(final SnomedConcept concept) {
                return Integer.toString(Math.abs(Math.round(concept.candidateScore / 10))) + "%";
            }
        };
        final TextColumn<SnomedConcept> groupCol = new TextColumn<SnomedConcept>() {
            @Override
            public String getValue(final SnomedConcept concept) {
                return concept.mappingGroup;
            }
        };
        groupCol.setSortable(true);
        final TextColumn<SnomedConcept> semTypeCol = new TextColumn<SnomedConcept>() {
            @Override
            public String getValue(final SnomedConcept concept) {
                return concept.semanticType;
            }
        };

        table.addColumn(scoreCol, messages.confidenceHeader());
        table.addColumn(groupCol, messages.groupHeader());
        table.addColumn(codeCol, messages.codeHeader());
        table.addColumn(descriptiontCol, messages.conceptHeader());
        table.addColumn(negatedCol, messages.negatedHeader());
        table.addColumn(semTypeCol, messages.semanticTypeHeader());
        table.addColumn(phraseCol, messages.phraseHeader());

        final ListDataProvider<SnomedConcept> dataProvider = new ListDataProvider<>();
        dataProvider.addDataDisplay(table);
        final List<SnomedConcept> list = dataProvider.getList();

        // make sortable by SNOMED code
        final ColumnSortEvent.ListHandler<SnomedConcept> codeSortHandler = new ColumnSortEvent.ListHandler<>(list);
        codeSortHandler.setComparator(codeCol, new SnomedCodeComparator());
        table.addColumnSortHandler(codeSortHandler);

        // make sortable by SNOMED description
        final ColumnSortEvent.ListHandler<SnomedConcept> descriptionSortHandler =
                new ColumnSortEvent.ListHandler<>(list);
        descriptionSortHandler.setComparator(descriptiontCol, new SnomedDescriptionComparator());
        table.addColumnSortHandler(descriptionSortHandler);

        // make sortable by mapping group phrase
        final ColumnSortEvent.ListHandler<SnomedConcept> phraseSortHandler =
                new ColumnSortEvent.ListHandler<>(list);
        phraseSortHandler.setComparator(phraseCol, new PhraseComparator());
        table.addColumnSortHandler(phraseSortHandler);

        // make sortable by group
        final ColumnSortEvent.ListHandler<SnomedConcept> groupSortHandler =
                new ColumnSortEvent.ListHandler<>(list);
        groupSortHandler.setComparator(groupCol, new MappingGroupComparator());
        table.addColumnSortHandler(groupSortHandler);

        return list;
    }
}
