package org.vaadin.hene.popupbutton;

import com.vaadin.Application;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class PopupButtonApplication extends Application {

	@Override
	public void init() {
		final Window mainWindow = new Window("PopupButton Application");
		setMainWindow(mainWindow);

		GridLayout mainLayout = new GridLayout(2, 2);
		mainLayout.setStyleName("main");
		mainLayout.setSizeFull();
		mainLayout.setMargin(true);
		mainWindow.setContent(mainLayout);

		HorizontalLayout horizontalLayout = new HorizontalLayout();
		mainLayout.addComponent(horizontalLayout);
		horizontalLayout.addComponent(createPopupButton());

		PopupButton textCaptionButton = new PopupButton("Caption only");
		final TextField tf = new TextField("Multi-line TextField");
		tf.setRows(10);
		textCaptionButton.setComponent(tf);
		horizontalLayout.addComponent(textCaptionButton);

		PopupButton iconButton = new PopupButton(null);
		iconButton.setIcon(new ThemeResource("../runo/icons/16/users.png"));
		horizontalLayout.addComponent(iconButton);

		VerticalLayout userLayout = new VerticalLayout();
		userLayout.setWidth("100px");
		userLayout.setHeight("50px");
		iconButton.setComponent(userLayout);

		Button addUser = new Button("Add user");
		addUser.setStyleName(Button.STYLE_LINK);
		addUser.setIcon(new ThemeResource("../runo/icons/16/user.png"));
		userLayout.addComponent(addUser);
		userLayout.setComponentAlignment(addUser, Alignment.MIDDLE_LEFT);

		Button removeUser = new Button("Remove user");
		removeUser.setStyleName(Button.STYLE_LINK);
		removeUser.setIcon(new ThemeResource("../runo/icons/16/cancel.png"));
		userLayout.addComponent(removeUser);
		userLayout.setComponentAlignment(removeUser, Alignment.MIDDLE_LEFT);

		Button openSubwindowButton = new Button("Open subwindow",
				new ClickListener() {
					public void buttonClick(ClickEvent event) {
						Window w = new Window();
						w.center();
						w.addComponent(createPopupButton());
						mainWindow.addWindow(w);
					}
				});
		horizontalLayout.addComponent(openSubwindowButton);

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
		gl
				.addComponent(createIconButton("../runo/icons/32/document-delete.png"));
		gl.addComponent(createIconButton("../runo/icons/32/document-pdf.png"));
		gl.addComponent(createIconButton("../runo/icons/32/document-web.png"));
		gl.addComponent(createIconButton("../runo/icons/32/document-doc.png"));
		gl.addComponent(createIconButton("../runo/icons/32/document-ppt.png"));
		gl.addComponent(createIconButton("../runo/icons/32/document-xsl.png"));
		gl
				.addComponent(createIconButton("../runo/icons/32/document-image.png"));
		gl.addComponent(createIconButton("../runo/icons/32/document-txt.png"));

		popupButton.addComponent(gl);
		return popupButton;
	}

	private Button createIconButton(String icon) {
		Button b = new Button();
		b.setIcon(new ThemeResource(icon));
		b.setStyleName(Button.STYLE_LINK);
		return b;
	}
}
