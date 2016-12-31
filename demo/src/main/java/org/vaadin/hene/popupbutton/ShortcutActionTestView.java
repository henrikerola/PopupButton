package org.vaadin.hene.popupbutton;

import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Henri Kerola / Vaadin
 */
public class ShortcutActionTestView extends VerticalLayout implements View {

    public ShortcutActionTestView() {
        PopupButton popupButton = new PopupButton("Click me!");
        addComponent(popupButton);

        VerticalLayout popupLayout = new VerticalLayout();
        popupLayout.addComponent(new TextField());
        Button button = new Button("Click me!", event ->
            Notification.show("Hello World!")
        );

        // When the popup is open pressing the enter should show "Hello World!"
        // notification event when the button is not focused.
        button.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        popupLayout.addComponent(button);
        popupButton.setContent(popupLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
