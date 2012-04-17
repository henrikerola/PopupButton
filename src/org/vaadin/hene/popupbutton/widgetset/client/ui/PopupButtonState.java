package org.vaadin.hene.popupbutton.widgetset.client.ui;

import com.vaadin.terminal.gwt.client.Connector;
import com.vaadin.terminal.gwt.client.ui.button.ButtonState;

public class PopupButtonState extends ButtonState {

	private boolean popupVisible;
	private Connector popupPositionConnector;
	private int xOffset = 0;
	private int yOffset = 0;
	private boolean popupFixedPosition;

	public boolean isPopupVisible() {
		return popupVisible;
	}

	public void setPopupVisible(boolean popupVisible) {
		this.popupVisible = popupVisible;
	}
	
	public Connector getPopupPositionConnector() {
		return popupPositionConnector;
	}

	public void setPopupPositionConnector(Connector popupPositionConnector) {
		this.popupPositionConnector = popupPositionConnector;

	}

	public int getxOffset() {
		return xOffset;
	}

	public void setxOffset(int xOffset) {
		this.xOffset = xOffset;
	}

	public int getyOffset() {
		return yOffset;
	}

	public void setyOffset(int yOffset) {
		this.yOffset = yOffset;
	}

	public boolean isPopupFixedPosition() {
		return popupFixedPosition;
	}

	public void setPopupFixedPosition(boolean popupFixedPosition) {
		this.popupFixedPosition = popupFixedPosition;
	}
}
