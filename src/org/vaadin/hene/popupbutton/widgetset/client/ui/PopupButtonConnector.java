package org.vaadin.hene.popupbutton.widgetset.client.ui;

import java.util.Collections;
import java.util.List;

import org.vaadin.hene.popupbutton.PopupButton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.ConnectorHierarchyChangeEvent.ConnectorHierarchyChangeHandler;
import com.vaadin.client.HasComponentsConnector;
import com.vaadin.client.VCaption;
import com.vaadin.client.VCaptionWrapper;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.PostLayoutListener;
import com.vaadin.client.ui.VPopupView;
import com.vaadin.client.ui.button.ButtonConnector;
import com.vaadin.shared.ui.Connect;

@SuppressWarnings("serial")
@Connect(PopupButton.class)
public class PopupButtonConnector extends ButtonConnector implements
		HasComponentsConnector, ConnectorHierarchyChangeHandler,
		PostLayoutListener, NativePreviewHandler {

	protected PopupButtonServerRpc rpc = RpcProxy.create(
			PopupButtonServerRpc.class, this);

	protected boolean popupVisible = false;

	protected HandlerRegistration nativePreviewHandler;

	public PopupButtonConnector() {
		addConnectorHierarchyChangeHandler(this);
	}

	@Override
	public void init() {
		super.init();
		nativePreviewHandler = Event.addNativePreviewHandler(this);
	}

	@Override
	public void onUnregister() {
		super.onUnregister();
		if (nativePreviewHandler != null) {
			nativePreviewHandler.removeHandler();
			nativePreviewHandler = null;
		}
	}

	@Override
	public void onClick(ClickEvent event) {
		if (!popupVisible && isEnabled()) {
			rpc.setPopupVisible(true);
		}
		super.onClick(event);
	}

	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent) {
		super.onStateChanged(stateChangeEvent);
		getWidget().addStyleName(VPopupButton.CLASSNAME);

		// getWidget().position = uidl.getStringAttribute("position");
		// getWidget().xOffset = uidl.getIntAttribute("xoffset");
		// getWidget().yOffset = uidl.getIntAttribute("yoffset");

		if (getState().popupVisible) {
			if (getState().popupPositionConnector != null) {
				getWidget().popupPositionWidget = ((ComponentConnector) getState().popupPositionConnector)
						.getWidget();
			} else {
				getWidget().popupPositionWidget = null;
			}

			if (getState().styles != null && !getState().styles.isEmpty()) {
				final StringBuffer styleBuf = new StringBuffer();
				final String primaryName = getWidget().popup
						.getStylePrimaryName();
				styleBuf.append(primaryName);
				styleBuf.append(" ");
				styleBuf.append(VPopupView.CLASSNAME + "-popup");
				for (String style : getState().styles) {
					styleBuf.append(" ");
					styleBuf.append(primaryName);
					styleBuf.append("-");
					styleBuf.append(style);
				}
				getWidget().popup.setStyleName(styleBuf.toString());
			} else {
				getWidget().popup.setStyleName(getWidget().popup
						.getStylePrimaryName()
						+ " "
						+ VPopupView.CLASSNAME
						+ "-popup");
			}

			getWidget().popup.setVisible(false);
			getWidget().popup.show();
			popupVisible = true;
		} else {
			getWidget().setPopupInvisible();
			popupVisible = false;
		}
	}

	public void onConnectorHierarchyChange(ConnectorHierarchyChangeEvent event) {
		if (childrenComponentConnector == null) {
			getWidget().hidePopup();
			getWidget().popup.setWidget(null);
		} else {
			getWidget().popup.setVisible(false);
			getWidget().popup.show();
			getWidget().popup.setWidget(childrenComponentConnector.getWidget());
		}
	}

	@Override
	protected VPopupButton createWidget() {
		return GWT.create(VPopupButton.class);
	}

	@Override
	public VPopupButton getWidget() {
		return (VPopupButton) super.getWidget();
	}

	@Override
	public PopupButtonState getState() {
		return (PopupButtonState) super.getState();
	}

	public void updateCaption(ComponentConnector component) {
		if (VCaption.isNeeded(component.getState())) {
			if (getWidget().popup.getCaptionWrapper() != null) {
				getWidget().popup.getCaptionWrapper().updateCaption();
			} else {
				VCaptionWrapper captionWrapper = new VCaptionWrapper(component,
						getConnection());
				getWidget().popup.setWidget(captionWrapper);
				captionWrapper.updateCaption();
			}
		} else {
			if (getWidget().popup.getCaptionWrapper() != null) {
				getWidget().popup.setWidget((Widget) getWidget().popup
						.getCaptionWrapper().getWrappedConnector().getWidget());
			}
		}
	}

	private ComponentConnector childrenComponentConnector;

	public List<ComponentConnector> getChildComponents() {
		if (childrenComponentConnector == null) {
			return Collections.<ComponentConnector> emptyList();
		}
		return Collections.singletonList(childrenComponentConnector);
	}

	public void setChildComponents(List<ComponentConnector> children) {
		if (children.size() > 1) {
			throw new IllegalArgumentException("");
		}

		if (!children.isEmpty()) {
			childrenComponentConnector = children.get(0);
		} else {
			childrenComponentConnector = null;
		}
	}

	public HandlerRegistration addConnectorHierarchyChangeHandler(
			ConnectorHierarchyChangeHandler handler) {
		return ensureHandlerManager().addHandler(
				ConnectorHierarchyChangeEvent.TYPE, handler);
	}

	public void postLayout() {
		if (popupVisible) {
			getWidget().showPopup();
		} else {
			getWidget().setPopupInvisible();
		}
	}

	public void onPreviewNativeEvent(NativePreviewEvent event) {
		if (isEnabled()) {
			Element target = Element
					.as(event.getNativeEvent().getEventTarget());
			switch (event.getTypeInt()) {
			case Event.ONCLICK:
				if (getWidget().isOrHasChildOfButton(target)) {
					if (popupVisible) {
						rpc.setPopupVisible(false);
					}
				}
				break;
			case Event.ONMOUSEDOWN:
				if (!getWidget().isOrHasChildOfPopup(target)
						&& !getWidget().isOrHasChildOfConsole(target)
						&& !getWidget().isOrHasChildOfButton(target)) {
					if (popupVisible) {
						rpc.setPopupVisible(false);
					}
				}
				break;
			case Event.ONKEYPRESS:
				// if (getWidget().isOrHasChildOfPopup(target)) {
				// // Catch children that use keyboard, so we can unfocus
				// // them
				// // when
				// // hiding
				// activeChildren.add(target);
				// }
				break;
			default:
				break;
			}
		}
	}

	@Override
	public boolean delegateCaptionHandling() {
		return false;
	}

}
