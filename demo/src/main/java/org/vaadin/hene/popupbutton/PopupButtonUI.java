package org.vaadin.hene.popupbutton;

import com.vaadin.data.Property;
import org.vaadin.hene.popupbutton.PopupButton.PopupVisibilityEvent;
import org.vaadin.hene.popupbutton.PopupButton.PopupVisibilityListener;

import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("serial")
public class PopupButtonUI extends UI {

	@Override
	protected void init(VaadinRequest request) {
		getPage().setTitle("PopupButton Application");

		GridLayout mainLayout = new GridLayout(2, 2);
		mainLayout.setStyleName("main");
		mainLayout.setSizeFull();
		mainLayout.setMargin(true);
		setContent(mainLayout);

		HorizontalLayout horizontalLayout = new HorizontalLayout();
		mainLayout.addComponent(horizontalLayout);
		horizontalLayout.addComponent(createPopupButton());

		PopupButton textCaptionButton = new PopupButton("Caption only");
        textCaptionButton.setStyleName("style1");
        textCaptionButton.addStyleName("style2");
		final TextArea textArea = new TextArea("Multi-line TextField");
        textArea.setImmediate(true);
        textArea.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Notification.show("" + event.getProperty().getValue());
            }
        });
		textArea.setRows(10);
		textCaptionButton.setComponent(textArea);
		horizontalLayout.addComponent(textCaptionButton);

		PopupButton iconButton = new PopupButton();
		iconButton.setIcon(new ThemeResource("../runo/icons/16/users.png"));
		iconButton.setStyleName("style1 style2");
		horizontalLayout.addComponent(iconButton);

		VerticalLayout userLayout = new VerticalLayout();
		userLayout.setWidth("100px");
		userLayout.setHeight("50px");
		iconButton.setComponent(userLayout);

		Button addUser = new Button("Add user");
		addUser.setStyleName(Reindeer.BUTTON_LINK);
		addUser.setIcon(new ThemeResource("../runo/icons/16/user.png"));
		userLayout.addComponent(addUser);
		userLayout.setComponentAlignment(addUser, Alignment.MIDDLE_LEFT);

		Button removeUser = new Button("Remove user");
		removeUser.setStyleName(Reindeer.BUTTON_LINK);
		removeUser.setIcon(new ThemeResource("../runo/icons/16/cancel.png"));
		userLayout.addComponent(removeUser);
		userLayout.setComponentAlignment(removeUser, Alignment.MIDDLE_LEFT);

		PopupButton listenerButton = createPopupButton();
		listenerButton.setCaption("VisibilityListener");
		listenerButton
				.addPopupVisibilityListener(new PopupVisibilityListener() {
					public void popupVisibilityChange(PopupVisibilityEvent event) {
						String msg = "Popup closed";
						if (event.getPopupButton().isPopupVisible()) {
							msg = "Popup opened";
						}
						Notification.show(msg);
					}
				});
		horizontalLayout.addComponent(listenerButton);

		PopupButton comboBoxButton = new PopupButton("ComboBox in Popup");
		ComboBox cb = new ComboBox();
		cb.setInputPrompt("ComboBox");
		comboBoxButton.setContent(cb);
		horizontalLayout.addComponent(comboBoxButton);

		PopupButton tableButton = new PopupButton("Table in Popup");
		Table table = new Table();
		table.addContainerProperty("property1", String.class, "-");
		table.addContainerProperty("property2", String.class, "-");
		table.addItem();
		tableButton.setContent(table);
		horizontalLayout.addComponent(tableButton);


		Button openSubwindowButton = new Button("Open subwindow",
				new ClickListener() {
					public void buttonClick(ClickEvent event) {
						Window w = new Window();
						w.center();
						w.setContent(createPopupButton());
						addWindow(w);
					}
				});
		horizontalLayout.addComponent(openSubwindowButton);

        horizontalLayout.addComponent(new PopupButton("No content"));

		Alignment[] aligns = new Alignment[] { Alignment.TOP_RIGHT,
				Alignment.BOTTOM_LEFT, Alignment.BOTTOM_RIGHT };
		for (Alignment align : aligns) {
			PopupButton b = createPopupButton();
			mainLayout.addComponent(b);
			mainLayout.setComponentAlignment(b, align);
		}
	}

	private PopupButton createPopupButton() {
		PopupButton popupButton = new PopupButton("Add");
		popupButton.setIcon(new ThemeResource(
				"../runo/icons/16/document-add.png"));

		GridLayout gl = new GridLayout(4, 3);
		gl.setWidth("150px");
		gl.setHeight("100px");
		gl.addComponent(createIconButton("../runo/icons/32/document.png"));
		gl.addComponent(createIconButton("../runo/icons/32/document-delete.png"));
		gl.addComponent(createIconButton("../runo/icons/32/document-pdf.png"));
		gl.addComponent(createIconButton("../runo/icons/32/document-web.png"));
		gl.addComponent(createIconButton("../runo/icons/32/document-doc.png"));
		gl.addComponent(createIconButton("../runo/icons/32/document-ppt.png"));
		gl.addComponent(createIconButton("../runo/icons/32/document-xsl.png"));
		gl.addComponent(createIconButton("../runo/icons/32/document-image.png"));
		gl.addComponent(createIconButton("../runo/icons/32/document-txt.png"));

		popupButton.setContent(gl);
		return popupButton;
	}

	private Button createIconButton(String icon) {
		Button b = new Button();
		b.setIcon(new ThemeResource(icon));
		b.setStyleName(Reindeer.BUTTON_LINK);
		return b;
	}
}
