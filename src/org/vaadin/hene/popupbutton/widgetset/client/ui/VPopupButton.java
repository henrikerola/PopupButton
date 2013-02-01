package org.vaadin.hene.popupbutton.widgetset.client.ui;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.VCaptionWrapper;
import com.vaadin.client.ui.VButton;
import com.vaadin.client.ui.VOverlay;
import com.vaadin.client.ui.VRichTextArea;

// This class contains code from the VPopupView class.  
public class VPopupButton extends VButton {

	/** Set the CSS class name to allow styling. */
	public static final String CLASSNAME = "v-popupbutton";

	public static final String POPUP_INDICATOR_CLASSNAME = "v-popup-indicator";

	final LayoutPopup popup = new LayoutPopup();

	String position = "auto";

	int xOffset = 0;

	int yOffset = 0;

	protected Widget popupPositionWidget;

	public VPopupButton() {
		super();
		DivElement e = Document.get().createDivElement();
		e.setClassName(POPUP_INDICATOR_CLASSNAME);
		getElement().getFirstChildElement().appendChild(e);
	}

	private Widget getPopupPositionWidget() {
		if (popupPositionWidget != null) {
			return popupPositionWidget;
		} else {
			return this;
		}
	}

	void showPopup() {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			public void execute() {
				if (position.equals("auto")) {
					int extra = 20;

					int left = getPopupPositionWidget().getAbsoluteLeft();
					int top = getPopupPositionWidget().getAbsoluteTop()
							+ getPopupPositionWidget().getOffsetHeight();
					int browserWindowWidth = Window.getClientWidth()
							+ Window.getScrollLeft();
					int browserWindowHeight = Window.getClientHeight()
							+ Window.getScrollTop();
					if (left + popup.getOffsetWidth() > browserWindowWidth
							- extra) {
						left = getPopupPositionWidget().getAbsoluteLeft()
								- (popup.getOffsetWidth() - getPopupPositionWidget()
										.getOffsetWidth());
					}
					if (top + popup.getOffsetHeight() > browserWindowHeight
							- extra) {
						top = getPopupPositionWidget().getAbsoluteTop()
								- popup.getOffsetHeight() - 2;
					}
					left = left + xOffset;
					if (left < 0) {
						left = 0;
					}
					popup.setPopupPosition(left, top + yOffset);
					popup.setVisible(true);
				} else if (position.equals("fixed")) {
					int extra = 20;

					int left = getPopupPositionWidget().getAbsoluteLeft();
					int top = getPopupPositionWidget().getAbsoluteTop()
							+ getPopupPositionWidget().getOffsetHeight()
							- Window.getScrollTop();

					int browserWindowWidth = Window.getClientWidth()
							+ Window.getScrollLeft();
					int clientHeight = Window.getClientHeight();
					if (left + popup.getOffsetWidth() > browserWindowWidth
							- extra) {
						left = getPopupPositionWidget().getAbsoluteLeft()
								- (popup.getOffsetWidth() - getPopupPositionWidget()
										.getOffsetWidth());
					}
					if (top + popup.getOffsetHeight() > clientHeight - extra) {
						top = (getPopupPositionWidget().getAbsoluteTop() - Window
								.getScrollTop()) - popup.getOffsetHeight() - 2;
					}
					left = left + xOffset;
					if (left < 0) {
						left = 0;
					}
					popup.setPopupPosition(left, top + yOffset);
					popup.addStyleName("fixed");
					popup.setShadowStyle("fixed");
					popup.setVisible(true);
				}
			}
		});
	}

	void hidePopup() {
		popup.setVisible(false);
		popup.hide();
	}

	private static native void nativeBlur(Element e)
	/*-{
	    if (e && e.blur) {
	        e.blur();
	    }
	}-*/;

	class LayoutPopup extends VOverlay {

		public static final String CLASSNAME = VPopupButton.CLASSNAME
				+ "-popup";

		private final Set<Element> activeChildren = new HashSet<Element>();

		private boolean hiding = false;

		public LayoutPopup() {
			super(false, false, true);
			setStyleName(CLASSNAME);
		}

		VCaptionWrapper getCaptionWrapper() {
			if (getWidget() instanceof VCaptionWrapper) {
				return (VCaptionWrapper) getWidget();
			}
			return null;
		}

		/*
		 * 
		 * We need a hack make popup act as a child of VPopupButton in Vaadin's
		 * component tree, but work in default GWT manner when closing or
		 * opening.
		 * 
		 * (non-Javadoc)
		 * 
		 * @see com.google.gwt.user.client.ui.Widget#getParent()
		 */
		@Override
		public Widget getParent() {
			if (!isAttached() || hiding) {
				return super.getParent();
			} else {
				return VPopupButton.this;
			}
		}

		@Override
		protected void onDetach() {
			super.onDetach();
			hiding = false;
		}

		@Override
		public void hide(boolean autoClosed) {
			hiding = true;
			syncChildren();
			super.hide(autoClosed);
		}

		@Override
		public void show() {
			hiding = false;
			super.show();
		}

		/**
		 * Try to sync all known active child widgets to server
		 */
		private void syncChildren() {
			// Notify children with focus
			if ((getWidget() instanceof Focusable)) {
				((Focusable) getWidget()).setFocus(false);
			} else {
				checkForRTE(getWidget());
			}

			// Notify children that have used the keyboard
			for (Element e : activeChildren) {
				try {
					nativeBlur(e);
				} catch (Exception ignored) {
				}
			}
			activeChildren.clear();
		}

		private void checkForRTE(Widget popupComponentWidget2) {
			if (popupComponentWidget2 instanceof VRichTextArea) {
				((VRichTextArea) popupComponentWidget2)
						.synchronizeContentToServer();
			} else if (popupComponentWidget2 instanceof HasWidgets) {
				HasWidgets hw = (HasWidgets) popupComponentWidget2;
				Iterator<Widget> iterator = hw.iterator();
				while (iterator.hasNext()) {
					checkForRTE(iterator.next());
				}
			}
		}

		@Override
		public com.google.gwt.user.client.Element getContainerElement() {
			return super.getContainerElement();
		}

		@Override
		protected void setShadowStyle(String style) {
			super.setShadowStyle(style);
		}
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		hidePopup();
	}
	
	public boolean isOrHasChildOfPopup(Element element) {
		return popup.getElement().isOrHasChild(element);
	}

	public boolean isOrHasChildOfButton(Element element) {
		return getElement().isOrHasChild(element);
	}

// Unclear what should happen here. ApplicationConnection.getConsole() gone
	public boolean isOrHasChildOfConsole(Element element) {
//		Console console = ApplicationConnection.getConsole();
//		return console instanceof VDebugConsole
//				&& ((VDebugConsole) console).getElement().isOrHasChild(
//						element);
		return false;
	}
}
