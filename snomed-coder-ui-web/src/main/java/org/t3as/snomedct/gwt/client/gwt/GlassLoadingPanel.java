package org.t3as.snomedct.gwt.client.gwt;

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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;

public class GlassLoadingPanel extends PopupPanel {

    private static final Resources resources = GWT.create(Resources.class);

    public GlassLoadingPanel() {
        // don't close it when clicking outside the widget
        super(false);

        // cover the entire screen with a glass panel to disable all other interaction
        setGlassEnabled(true);

        // just show a spinner
        setWidget(new Image(resources.ajaxLoading()));
    }

    public void setPositionAndShow() {
        setPopupPositionAndShow(new PopupPanel.PositionCallback() {
            public void setPosition(final int offsetWidth, final int offsetHeight) {
                final int left = (Window.getClientWidth() - offsetWidth) / 2;
                final int top = (Window.getClientHeight() - offsetHeight) / 2;
                setPopupPosition(left, top);
            }
        });
    }
}
