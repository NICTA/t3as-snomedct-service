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

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import org.t3as.snomedct.gwt.client.Messages;
import org.t3as.snomedct.gwt.client.SemanticType;

import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

public class ConfigureHandler implements ClickHandler {

    private static final int GLASS_TOP_MARGIN_PX = 30;
    private static final int GLASS_LEFT_MARGIN_PX = 40;

    private final ConfigurePopup popup;

    public ConfigureHandler(final List<SemanticType> types, final Map<String, String> typeCodeToDescription) {
        popup = new ConfigurePopup(types, typeCodeToDescription);
    }

    @Override
    public void onClick(final ClickEvent event) {
        popup.setPositionAndShow();
    }

    private static class ConfigurePopup extends PopupPanel {

        private final Messages messages = GWT.create(Messages.class);

        ConfigurePopup(final List<SemanticType> types, final Map<String, String> typeCodeToDescription) {
            // close when clicking outside the widget
            super(true);

            // cover the entire screen with a glass panel to disable all other interaction
            setGlassEnabled(true);

            final CellTable<SemanticType> table = new CellTable<>();

            // setup the columns
            final Column<SemanticType, Boolean> checkCol =
                    new Column<SemanticType, Boolean>(new CheckboxCell(true, true)) {
                        @Override
                        public Boolean getValue(final SemanticType type) {
                            return type.isSelected();
                        }
                    };
            checkCol.setFieldUpdater(new FieldUpdater<SemanticType, Boolean>() {
                @Override
                public void update(final int index, final SemanticType type, final Boolean value) {
                    type.setSelected(value);
                }
            });
            table.addColumn(checkCol);

            final TextColumn<SemanticType> codeCol = new TextColumn<SemanticType>() {
                @Override
                public String getValue(final SemanticType type) {
                    return type.getCode();
                }
            };
            table.addColumn(codeCol);

            final TextColumn<SemanticType> descriptionCol = new TextColumn<SemanticType>() {
                @Override
                public String getValue(final SemanticType type) {
                    return type.getDescription();
                }
            };
            table.addColumn(descriptionCol);

            // the DataProvider to get the data async
            final SemanticTypeDataProvider dataProvider = new SemanticTypeDataProvider(types, typeCodeToDescription);
            dataProvider.addDataDisplay(table);

            // setup the user interface
            final TickUntickAllClickHandler tickAllClickHandler =
                    new TickUntickAllClickHandler(true, types, dataProvider);
            final TickUntickAllClickHandler untickAllClickHandler =
                    new TickUntickAllClickHandler(false, types, dataProvider);
            final DefaultsClickHandler defaultsClickHandler = new DefaultsClickHandler(types, dataProvider);

            final VerticalPanel vp = new VerticalPanel();

            // top buttons
            final Button tickAllTop = new Button(messages.tickAllButton());
            tickAllTop.addClickHandler(tickAllClickHandler);
            final Button untickAllTop = new Button(messages.untickAllButton());
            untickAllTop.addClickHandler(untickAllClickHandler);
            final Button defaultsButtonTop = new Button(messages.defaultsButton());
            defaultsButtonTop.addClickHandler(defaultsClickHandler);
            final Button okTop = new Button(messages.okButton());
            okTop.addClickHandler(new OkClickHandler(this));
            final HorizontalPanel topTickPanel = new HorizontalPanel();
            topTickPanel.setSpacing(5);
            topTickPanel.add(tickAllTop);
            topTickPanel.add(untickAllTop);
            topTickPanel.add(defaultsButtonTop);
            final HorizontalPanel topButtons = new HorizontalPanel();
            topButtons.setWidth("100%");
            topButtons.setSpacing(5);
            topButtons.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
            topButtons.add(topTickPanel);
            topButtons.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
            topButtons.add(okTop);
            vp.add(topButtons);

            vp.add(table);

            // bottom buttons
            final Button tickAllBottom = new Button(messages.tickAllButton());
            tickAllBottom.addClickHandler(tickAllClickHandler);
            final Button untickAllBottom = new Button(messages.untickAllButton());
            untickAllBottom.addClickHandler(untickAllClickHandler);
            final Button defaultsButtonBottom = new Button(messages.defaultsButton());
            defaultsButtonBottom.addClickHandler(defaultsClickHandler);
            final Button okBottom = new Button(messages.okButton());
            okBottom.addClickHandler(new OkClickHandler(this));
            final HorizontalPanel bottomTickPanel = new HorizontalPanel();
            bottomTickPanel.setSpacing(5);
            bottomTickPanel.add(tickAllBottom);
            bottomTickPanel.add(untickAllBottom);
            bottomTickPanel.add(defaultsButtonBottom);
            final HorizontalPanel bottomButtons = new HorizontalPanel();
            bottomButtons.setWidth("100%");
            bottomButtons.setSpacing(5);
            bottomButtons.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
            bottomButtons.add(bottomTickPanel);
            bottomButtons.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
            bottomButtons.add(okBottom);
            vp.add(bottomButtons);

            final StackPanel stack = new StackPanel();
            stack.add(vp, messages.configureSemanticTypesHeading());

            setWidget(new ScrollPanel(stack));
        }

        public void setPositionAndShow() {
            setPopupPositionAndShow(new PopupPanel.PositionCallback() {
                public void setPosition(final int offsetWidth, final int offsetHeight) {
                    // don't let the popup be too wide or tall
                    final int width = Math.min(offsetWidth, Window.getClientWidth() - GLASS_LEFT_MARGIN_PX * 2);
                    final int height = Math.min(offsetHeight, Window.getClientHeight() - GLASS_TOP_MARGIN_PX * 2);
                    setPixelSize(width, height);

                    // centre of the screen regardless of size
                    setPopupPosition((Window.getClientWidth() - width) / 2, (Window.getClientHeight() - height) / 2);
                }
            });
        }
    }

    private static class SemanticTypeDataProvider extends AsyncDataProvider<SemanticType> {
        private final String webserviceUrl;
        private final List<SemanticType> types;
        private final Map<String, String> typeCodeToDescription;

        private SemanticTypeDataProvider(final List<SemanticType> types,
                                         final Map<String, String> typeCodeToDescription) {
            this.types = types;
            this.typeCodeToDescription = typeCodeToDescription;
            this.webserviceUrl = "http://" + Cookies.getCookie("webserviceHost")
                                 + "/snomed-coder-web/rest/v1.0/semanticTypes";
        }

        @Override
        protected void onRangeChanged(final HasData<SemanticType> display) {
            final RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, webserviceUrl);
            builder.setHeader("Accept", MediaType.APPLICATION_JSON);
            builder.setCallback(new RequestCallback() {
                @Override
                public void onResponseReceived(final Request request, final Response response) {
                    final JsArray<SemanticType> receivedTypes = JsonUtils.safeEval(response.getText());
                    types.clear();
                    for (int i = 0; i < receivedTypes.length(); i++) {
                        final SemanticType type = receivedTypes.get(i);
                        type.setSelected(type.isOnByDefault());
                        types.add(type);
                        typeCodeToDescription.put(type.getCode(), type.getDescription());
                    }
                    updateRowData(0, types);
                    for (final HasData<SemanticType> display : getDataDisplays()) {
                        display.setVisibleRange(0, types.size());
                    }
                }

                @Override
                public void onError(final Request request, final Throwable exception) {
                    // TODO finish this
                }
            });
            try { builder.send(); }
            catch (final RequestException e) {
                // TODO: figure out what to do here
                e.printStackTrace();
            }
        }
    }

    private static class TickUntickAllClickHandler implements ClickHandler {
        private final boolean tick;
        private final List<SemanticType> types;
        private final SemanticTypeDataProvider dataProvider;

        public TickUntickAllClickHandler(final boolean tick, final List<SemanticType> types,
                                         final SemanticTypeDataProvider dataProvider) {
            this.tick = tick;
            this.types = types;
            this.dataProvider = dataProvider;
        }

        @Override
        public void onClick(final ClickEvent event) {
            // tick or untick all the checkboxes
            for (final SemanticType t : types) {
                t.setSelected(tick);
            }
            dataProvider.updateRowData(0, types);
        }
    }

    private static class DefaultsClickHandler implements ClickHandler {
        private final List<SemanticType> types;
        private final SemanticTypeDataProvider dataProvider;

        public DefaultsClickHandler(final List<SemanticType> types, final SemanticTypeDataProvider dataProvider) {
            this.types = types;
            this.dataProvider = dataProvider;
        }

        @Override
        public void onClick(final ClickEvent event) {
            for (final SemanticType t : types) {
                t.setSelected(t.isOnByDefault());
            }
            dataProvider.updateRowData(0, types);
        }
    }

    private static class OkClickHandler implements ClickHandler {
        private final ConfigurePopup popup;

        public OkClickHandler(final ConfigurePopup popup) { this.popup = popup; }

        @Override
        public void onClick(final ClickEvent event) {
            popup.hide();
        }
    }
}
