package org.vaadin.hene.popupbutton;

import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;

@SuppressWarnings("serial")
@Title("PopupButton Application")
public class PopupButtonUI extends UI {

    @Override
    protected void init(VaadinRequest request) {

        Navigator navigator = new Navigator(this, this);
        navigator.addView("", DefaultView.class);
        navigator.addView("svgtest", SvgTestView.class);
        navigator.addView("shortcutactiontest", ShortcutActionTestView.class);
    }
}
