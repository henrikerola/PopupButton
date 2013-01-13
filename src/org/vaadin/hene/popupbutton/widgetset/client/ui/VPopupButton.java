package org.vaadin.hene.popupbutton.widgetset.client.ui;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Console;
import com.vaadin.terminal.gwt.client.Container;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.RenderInformation.Size;
import com.vaadin.terminal.gwt.client.RenderSpace;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.Util;
import com.vaadin.terminal.gwt.client.VCaption;
import com.vaadin.terminal.gwt.client.VCaptionWrapper;
import com.vaadin.terminal.gwt.client.VDebugConsole;
import com.vaadin.terminal.gwt.client.ui.VButton;
import com.vaadin.terminal.gwt.client.ui.VOverlay;
import com.vaadin.terminal.gwt.client.ui.VPopupView;
import com.vaadin.terminal.gwt.client.ui.richtextarea.VRichTextArea;

// This class contains code from the VPopupView class.  
public class VPopupButton extends VButton implements Container,
		Iterable<Widget> {

	/** Set the CSS class name to allow styling. */
	public static final String CLASSNAME = "v-popupbutton";

	public static final String POPUP_INDICATOR_CLASSNAME = "v-popup-indicator";

	private final LayoutPopup popup = new LayoutPopup();

	private boolean popupVisible = false;

	private String position = "auto";

	private int xOffset = 0;

	private int yOffset = 0;

	private Paintable popupPositionPaintable;

	public VPopupButton() {
		super();
		DivElement e = Document.get().createDivElement();
		e.setClassName(POPUP_INDICATOR_CLASSNAME);
		getElement().getFirstChildElement().appendChild(e);
	}

	/**
	 * Called whenever an update is received from the server
	 */
	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		super.updateFromUIDL(uidl, client);
		if (client.updateComponent(this, uidl, false)) {
			hidePopup();
			return;
		}
		addStyleName(CLASSNAME);

		position = uidl.getStringAttribute("position");
		xOffset = uidl.getIntAttribute("xoffset");
		yOffset = uidl.getIntAttribute("yoffset");

		popupVisible = uidl.getBooleanVariable("popupVisible");
		if (popupVisible) {
			if (uidl.hasAttribute("popupPositionPaintable")) {
				popupPositionPaintable = uidl.getPaintableAttribute(
						"popupPositionPaintable", client);
			} else {
				popupPositionPaintable = null;
			}

			if (uidl.hasAttribute("style")) {
				final String[] styles = uidl.getStringAttribute("style").split(
						" ");
				final StringBuffer styleBuf = new StringBuffer();
				final String primaryName = popup.getStylePrimaryName();
				styleBuf.append(primaryName);
				styleBuf.append(" ");
				styleBuf.append(VPopupView.CLASSNAME + "-popup");
				for (int i = 0; i < styles.length; i++) {
					styleBuf.append(" ");
					styleBuf.append(primaryName);
					styleBuf.append("-");
					styleBuf.append(styles[i]);
				}
				popup.setStyleName(styleBuf.toString());
			} else {
				popup.setStyleName(popup.getStylePrimaryName() + " "
						+ VPopupView.CLASSNAME + "-popup");
			}

			UIDL popupUIDL = uidl.getChildUIDL(0);
			popup.setVisible(false);
			popup.show();
			popup.updateFromUIDL(popupUIDL);
			showPopup();
		} else {
			hidePopup();
		}
	}

	@Override
	public void onClick(ClickEvent event) {
		if (!popupVisible) {
			updateState(true, false);
		}
		super.onClick(event);
	}

	private void updateState(boolean visible, boolean immediate) {
		client.updateVariable(id, "popupVisible", visible, immediate);
	}

	private Widget getPopupPositionWidget() {
		if (popupPositionPaintable != null) {
			return (Widget) popupPositionPaintable;
		} else {
			return this;
		}
	}

	private void showPopup() {
		if (position.equals("auto")) {
			int extra = 20;

			int left = getPopupPositionWidget().getAbsoluteLeft();
			int top = getPopupPositionWidget().getAbsoluteTop()
					+ getPopupPositionWidget().getOffsetHeight();
			int browserWindowWidth = Window.getClientWidth()
					+ Window.getScrollLeft();
			int browserWindowHeight = Window.getClientHeight()
					+ Window.getScrollTop();
			if (left + popup.getOffsetWidth() > browserWindowWidth - extra) {
				left = getPopupPositionWidget().getAbsoluteLeft()
						- (popup.getOffsetWidth() - getPopupPositionWidget()
								.getOffsetWidth());
			}
			if (top + popup.getOffsetHeight() > browserWindowHeight - extra) {
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
			if (left + popup.getOffsetWidth() > browserWindowWidth - extra) {
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

	private void hidePopup() {
		popup.setVisible(false);
		popup.hide();
	}

	private static native void nativeBlur(Element e)
	/*-{
	    if (e && e.blur) {
	        e.blur();
	    }
	}-*/;

	private class LayoutPopup extends VOverlay {

		public static final String CLASSNAME = VPopupButton.CLASSNAME
				+ "-popup";

		private final Set<Element> activeChildren = new HashSet<Element>();

		private boolean hiding = false;

		public LayoutPopup() {
			super(false, false, true);
			setStyleName(CLASSNAME);
		}

		public void updateFromUIDL(final UIDL uidl) {
			if (Util.isCached(uidl.getChildUIDL(0))) {
				return;
			}

			Paintable newPopupComponent = client.getPaintable(uidl
					.getChildUIDL(0));
			if (!newPopupComponent.equals(getPaintable())) {
				if (getPaintable() != null) {
					client.unregisterPaintable(getPaintable());
				}
				setWidget((Widget) newPopupComponent);
			}
			getPaintable().updateFromUIDL(uidl.getChildUIDL(0), client);
		}

		private Paintable getPaintable() {
			return (Paintable) getWidget();
		}

		private VCaptionWrapper getCaptionWrapper() {
			if (getWidget() instanceof VCaptionWrapper) {
				return (VCaptionWrapper) getWidget();
			}
			return null;
		}

		@Override
		protected void onPreviewNativeEvent(NativePreviewEvent event) {
			Element target = Element
					.as(event.getNativeEvent().getEventTarget());
			switch (event.getTypeInt()) {
			case Event.ONCLICK:
				if (isOrHasChildOfButton(target)) {
					updateState(false, true);
				}
				break;
			case Event.ONMOUSEDOWN:
				if (!isOrHasChildOfPopup(target)
						&& !isOrHasChildOfConsole(target)
						&& !isOrHasChildOfButton(target)) {
					updateState(false, true);
				}
				break;
			case Event.ONKEYPRESS:
				if (isOrHasChildOfPopup(target)) {
					// Catch children that use keyboard, so we can unfocus
					// them
					// when
					// hiding
					activeChildren.add(target);
				}
				break;
			default:
				break;
			}

			super.onPreviewNativeEvent(event);
		}

		private boolean isOrHasChildOfPopup(Element element) {
			return getElement().isOrHasChild(element);
		}

		private boolean isOrHasChildOfButton(Element element) {
			return VPopupButton.this.getElement().isOrHasChild(element);
		}

		private boolean isOrHasChildOfConsole(Element element) {
			Console console = ApplicationConnection.getConsole();
			return console instanceof VDebugConsole
					&& ((VDebugConsole) console).getElement().isOrHasChild(
							element);
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

		/*-
		private void unregisterPaintables() {
			if (getPaintable() != null) {
				client.unregisterPaintable(getPaintable());
			}
		}*/

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
		public void updateShadowSizeAndPosition() {
			super.updateShadowSizeAndPosition();
		}

		@Override
		protected void setShadowStyle(String style) {
			super.setShadowStyle(style);
		}
	}

	public RenderSpace getAllocatedSpace(Widget child) {
		Size popupExtra = calculatePopupExtra();

		return new RenderSpace(RootPanel.get().getOffsetWidth()
				- popupExtra.getWidth(), RootPanel.get().getOffsetHeight()
				- popupExtra.getHeight());
	}

	/**
	 * Calculate extra space taken by the popup decorations
	 * 
	 * @return
	 */
	protected Size calculatePopupExtra() {
		Element pe = popup.getElement();
		Element ipe = popup.getContainerElement();

		// border + padding
		int width = Util.getRequiredWidth(pe) - Util.getRequiredWidth(ipe);
		int height = Util.getRequiredHeight(pe) - Util.getRequiredHeight(ipe);

		return new Size(width, height);
	}

	public boolean hasChildComponent(Widget component) {
		if (popup.getWidget() != null) {
			return popup.getWidget() == component;
		} else {
			return false;
		}
	}

	public void replaceChildComponent(Widget oldComponent, Widget newComponent) {
		if (!hasChildComponent(oldComponent)) {
			throw new IllegalArgumentException();
		}
		popup.setWidget(newComponent);
	}

	public boolean requestLayout(Set<Paintable> children) {
		popup.updateShadowSizeAndPosition();
		return true;
	}

	public void updateCaption(Paintable component, UIDL uidl) {
		if (VCaption.isNeeded(uidl)) {
			if (popup.getCaptionWrapper() != null) {
				popup.getCaptionWrapper().updateCaption(uidl);
			} else {
				VCaptionWrapper captionWrapper = new VCaptionWrapper(component,
						client);
				popup.setWidget(captionWrapper);
				captionWrapper.updateCaption(uidl);
			}
		} else {
			if (popup.getCaptionWrapper() != null) {
				popup.setWidget((Widget) popup.getCaptionWrapper()
						.getPaintable());
			}
		}
	}

	public Iterator<Widget> iterator() {
		return new Iterator<Widget>() {

			int pos = 0;

			public boolean hasNext() {
				// There is a child widget only if next() has not been called.
				return (pos == 0);
			}

			public Widget next() {
				// Next can be called only once to return the popup.
				if (pos != 0) {
					throw new NoSuchElementException();
				}
				pos++;
				return popup;
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		hidePopup();
	}
}
