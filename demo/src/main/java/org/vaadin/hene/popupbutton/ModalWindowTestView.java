package org.vaadin.hene.popupbutton;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * @author Henri Kerola / Vaadin
 */
public class ModalWindowTestView extends VerticalLayout implements View {

    public ModalWindowTestView() {

        final PopupButton popupButton = new PopupButton("Click me!");
        popupButton.setContent(new Button("Open modal window", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Window window = new Window();
                window.setModal(true);
                getUI().addWindow(window);
                popupButton.setPopupVisible(false);
            }
        }));
        addComponent(popupButton);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
