package org.vaadin.hene.popupbutton;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Henri Kerola / Vaadin
 */
public class GridInPopupTest extends VerticalLayout implements View {

    public GridInPopupTest() {

        // No client-side exceptions should be seen in the debug window
        // when opening and closing the popup.

        PopupButton popupButton = new PopupButton("Click me!");

        Grid grid = new Grid();
        popupButton.setContent(grid);

        addComponent(popupButton);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
