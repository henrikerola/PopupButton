package org.vaadin.hene.popupbutton;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Iterator;

import org.vaadin.hene.popupbutton.widgetset.client.ui.PopupButtonServerRpc;
import org.vaadin.hene.popupbutton.widgetset.client.ui.PopupButtonState;

import com.vaadin.util.ReflectTools;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

/**
 * Server side component for the VPopupButton widget.
 */
// This class contains code from AbstractComponentContainer
public class PopupButton extends Button implements ComponentContainer {

	private static final long serialVersionUID = -3148268967211155218L;

	private static final Method COMPONENT_ATTACHED_METHOD = ReflectTools
			.findMethod(ComponentAttachListener.class,
					"componentAttachedToContainer", ComponentAttachEvent.class);

	private static final Method COMPONENT_DETACHED_METHOD = ReflectTools
			.findMethod(ComponentDetachListener.class,
					"componentDetachedFromContainer",
					ComponentDetachEvent.class);

	private static final Method POPUP_VISIBILITY_METHOD = ReflectTools
			.findMethod(PopupVisibilityListener.class, "popupVisibilityChange",
					PopupVisibilityEvent.class);

	private Component component;

	// These can be used by extending PopupButton.
	// It's possible that these are removed in future versions or functionality
	// is changed.
	protected int xOffset = 0;
	protected int yOffset = 0;
	protected boolean popupFixedPosition;
	//protected Paintable popupPositionPaintable; //FIXME
	
	private PopupButtonServerRpc rpc = new PopupButtonServerRpc() {
		
		public void setPopupVisible(boolean visible) {
			PopupButton.this.setPopupVisible(visible);
		}
	};

	public PopupButton() {
		registerRpc(rpc);
	}

	public PopupButton(String caption) {
		super(caption);
		registerRpc(rpc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.ui.ComponentContainer#addComponent(com.vaadin.ui.Component)
	 */
	public void addComponent(Component c) {
		component = c;
		markAsDirty();

		if (c instanceof ComponentContainer) {
			// Make sure we're not adding the component inside it's own content
			for (Component parent = this; parent != null; parent = parent
					.getParent()) {
				if (parent == c) {
					throw new IllegalArgumentException(
							"Component cannot be added inside it's own content");
				}
			}
		}

		if (c.getParent() != null) {
			// If the component already has a parent, try to remove it
			ComponentContainer oldParent = (ComponentContainer) c.getParent();
			oldParent.removeComponent(c);

		}

		c.setParent(this);
		fireEvent(new ComponentAttachEvent(this, component));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vaadin.ui.ComponentContainer#getComponentIterator()
	 */
	public Iterator<Component> getComponentIterator() {
		return new Iterator<Component>() {

			private boolean first = component == null;

			public boolean hasNext() {
				return !first;
			}

			public Component next() {
				if (!first) {
					first = true;
					return component;
				} else {
					return null;
				}
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	/**
	 * Not supported in this implementation.
	 * 
	 * @see com.vaadin.ui.ComponentContainer#moveComponentsFrom(com.vaadin.ui.
	 *      ComponentContainer)
	 */
	public void moveComponentsFrom(ComponentContainer source) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vaadin.ui.ComponentContainer#removeAllComponents()
	 */
	public void removeAllComponents() {
		if (component != null) {
			removeComponent(component);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.ui.ComponentContainer#removeComponent(com.vaadin.ui.Component)
	 */
	public void removeComponent(Component c) {
		if (c.getParent() == this) {
			c.setParent(null);
			fireEvent(new ComponentDetachEvent(this, c));
		}
		component = null;
		markAsDirty();
	}

	/**
	 * 
	 * Not supported in this implementation.
	 * 
	 * @see com.vaadin.ui.ComponentContainer#replaceComponent(com.vaadin.ui.Component,
	 *      com.vaadin.ui.Component)
	 */
	public void replaceComponent(Component oldComponent, Component newComponent) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Shows or hides popup.
	 * 
	 * @param popupVisible
	 *            if true, popup is set to visible, otherwise popup is hidden.
	 */
	public void setPopupVisible(boolean popupVisible) {
		if (getState().popupVisible != popupVisible) {
			getState().popupVisible = popupVisible;
			fireEvent(new PopupVisibilityEvent(this));
			markAsDirty();
		}
	}

	/**
	 * Checks if the popup is visible.
	 * 
	 * @return true, if popup is visible, false otherwise.
	 */
	public boolean isPopupVisible() {
		return getState().popupVisible;
	}

	/**
	 * Set the content component of the popup.
	 * 
	 * @param component
	 *            the component to be displayed in the popup.
	 */
	public void setComponent(Component component) {
		addComponent(component);
	}

	/**
	 * Add a listener that is called whenever the visibility of the popup is
	 * changed.
	 * 
	 * @param listener
	 *            the listener to add
	 * @see PopupVisibilityListener
	 * @see PopupVisibilityEvent
	 * @see #removePopupVisibilityListener(PopupVisibilityListener)
	 * 
	 */
	public void addPopupVisibilityListener(PopupVisibilityListener listener) {
		addListener(PopupVisibilityEvent.class, listener,
				POPUP_VISIBILITY_METHOD);
	}

	/**
	 * Removes a previously added listener, so that it no longer receives events
	 * when the visibility of the popup changes.
	 * 
	 * @param listener
	 *            the listener to remove
	 * @see PopupVisibilityListener
	 * @see #addPopupVisibilityListener(PopupVisibilityListener)
	 */
	public void removePopupVisibilityListener(PopupVisibilityListener listener) {
		removeListener(PopupVisibilityEvent.class, listener,
				POPUP_VISIBILITY_METHOD);
	}

	/**
	 * This event is received by the PopupVisibilityListeners when the
	 * visibility of the popup changes. You can get the new visibility directly
	 * with {@link #isPopupVisible()}, or get the PopupButton that produced the
	 * event with {@link #getPopupButton()}.
	 * 
	 */
	public class PopupVisibilityEvent extends Event {

		private static final long serialVersionUID = 3170716121022820317L;

		public PopupVisibilityEvent(PopupButton source) {
			super(source);
		}

		/**
		 * Get the PopupButton instance that is the source of this event.
		 * 
		 * @return the source PopupButton
		 */
		public PopupButton getPopupButton() {
			return (PopupButton) getSource();
		}

		/**
		 * Returns the current visibility of the popup.
		 * 
		 * @return true if the popup is visible
		 */
		public boolean isPopupVisible() {
			return getPopupButton().isPopupVisible();
		}
	}

	/**
	 * Defines a listener that can receive a PopupVisibilityEvent when the
	 * visibility of the popup changes.
	 * 
	 */
	public interface PopupVisibilityListener extends Serializable {
		/**
		 * Pass to {@link PopupButton#PopupVisibilityEvent} to start listening
		 * for popup visibility changes.
		 * 
		 * @param event
		 *            the event
		 * 
		 * @see {@link PopupVisibilityEvent}
		 * @see {@link PopupButton#addPopupVisibilityListener(PopupVisibilityListener)}
		 */
		public void popupVisibilityChange(PopupVisibilityEvent event);
	}

	public boolean isComponentVisible(Component childComponent) {
		return true;
	}

	public Iterator<Component> iterator() {
		return getComponentIterator();
	}

	public int getComponentCount() {
		return (component != null ? 1 : 0);
	}
	
	@Override
	public PopupButtonState getState() {
		return (PopupButtonState) super.getState();
	}
	
	protected void setPopupPositionComponent(Component component) {
		getState().popupPositionConnector = component;
	}

	public void addComponentAttachListener(ComponentAttachListener listener) {
		addListener(ComponentContainer.ComponentAttachEvent.class, listener, COMPONENT_ATTACHED_METHOD);	
	}

	public void removeComponentAttachListener(ComponentAttachListener listener) {
		removeListener(ComponentContainer.ComponentAttachEvent.class, listener, COMPONENT_ATTACHED_METHOD);
	}

	public void addComponentDetachListener(ComponentDetachListener listener) {
		addListener(ComponentContainer.ComponentDetachEvent.class, listener, COMPONENT_DETACHED_METHOD);
	}

	public void removeComponentDetachListener(ComponentDetachListener listener) {
		removeListener(ComponentContainer.ComponentDetachEvent.class, listener, COMPONENT_DETACHED_METHOD);
	}

	public void addListener(ComponentAttachListener listener) {
		addComponentAttachListener(listener);
	}

	public void removeListener(ComponentAttachListener listener) {
		removeComponentAttachListener(listener);
	}

	public void addListener(ComponentDetachListener listener) {
		addComponentDetachListener(listener);
	}

	public void removeListener(ComponentDetachListener listener) {
		removeComponentDetachListener(listener);
	}
}
