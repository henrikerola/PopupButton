package org.vaadin.hene.popupbutton.widgetset.client.ui;

import com.vaadin.shared.Connector;
import com.vaadin.shared.annotations.DelegateToWidget;
import com.vaadin.shared.ui.button.ButtonState;

public class PopupButtonState extends ButtonState {

	public boolean popupVisible;
	public Connector popupPositionConnector;
	public int xOffset = 0;
	public int yOffset = 0;
	public boolean popupFixedPosition;

    @DelegateToWidget
    public int direction = 0;
}
