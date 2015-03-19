package org.vaadin.hene.popupbutton;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

/**
 * @author Henri Kerola / Vaadin
 */
public class PopupPositionTest extends VerticalLayout implements View {

    public PopupPositionTest() {

        CssLayout cssLayout = new CssLayout();
        cssLayout.setWidth("300px");

        MyPopupButton popupButton = new MyPopupButton(cssLayout);
        popupButton.setDirection(Alignment.BOTTOM_CENTER);
        popupButton.setContent(new Button("Hello"));
        cssLayout.addComponent(popupButton);

        addComponent(cssLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

    class MyPopupButton extends PopupButton {

        public MyPopupButton(Component c) {
            setPopupPositionComponent(c);
        }
    }
}
