package org.vaadin.hene.popupbutton;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Henri Kerola / Vaadin
 */
public class SvgTestView extends VerticalLayout implements View {


    public SvgTestView() {

        PopupButton popupButton = new PopupButton();
        addComponent(popupButton);


        Label label = new Label();
        label.setContentMode(ContentMode.HTML);
        label.setValue("<svg  xmlns=\"http://www.w3.org/2000/svg\"\n" +
                "      xmlns:xlink=\"http://www.w3.org/1999/xlink\">\n" +
                "    <rect x=\"10\" y=\"10\" height=\"100\" width=\"100\"\n" +
                "          style=\"stroke:#ff0000; fill: #0000ff\"/>\n" +
                "</svg>");
        popupButton.setContent(label);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
