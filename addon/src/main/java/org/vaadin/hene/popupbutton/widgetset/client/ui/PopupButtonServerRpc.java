package org.vaadin.hene.popupbutton.widgetset.client.ui;

import com.vaadin.shared.communication.ServerRpc;

public interface PopupButtonServerRpc extends ServerRpc {

    void setPopupVisible(boolean visible);

}
