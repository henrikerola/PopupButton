package org.vaadin.hene.popupbutton;

import com.vaadin.data.Property;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Henri Kerola / Vaadin
 */
public class DefaultView extends GridLayout implements View {

    private List<PopupButton> popupButtons = new ArrayList<PopupButton>();

    public DefaultView() {
        super(2, 2);
        setSizeFull();
        setMargin(true);


        HorizontalLayout horizontalLayout = new HorizontalLayout();
        addComponent(horizontalLayout);
        horizontalLayout.addComponent(createDirectionSelector());
        horizontalLayout.addComponent(createPopupButton());

        PopupButton textCaptionButton = new PopupButton("Caption only");
        popupButtons.add(textCaptionButton);
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
        textCaptionButton.addPopupVisibilityListener(new PopupButton.PopupVisibilityListener() {
            @Override
            public void popupVisibilityChange(PopupButton.PopupVisibilityEvent event) {
                if (event.isPopupVisible()) {
                    textArea.focus();
                }
            }
        });
        textArea.setRows(10);
        textCaptionButton.setComponent(textArea);
        horizontalLayout.addComponent(textCaptionButton);

        PopupButton iconButton = new PopupButton();
        popupButtons.add(iconButton);
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

        final PopupButton listenerButton = new PopupButton();
        popupButtons.add(listenerButton);
        final Button b = new Button("Click me!", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                listenerButton.setPopupVisible(false);
                Notification.show("Button clicked!");
            }
        });
        listenerButton.setContent(b);
        listenerButton.setCaption("VisibilityListener");
        listenerButton
                .addPopupVisibilityListener(new PopupButton.PopupVisibilityListener() {
                    public void popupVisibilityChange(PopupButton.PopupVisibilityEvent event) {
                        String msg = "Popup closed";
                        if (event.getPopupButton().isPopupVisible()) {
                            msg = "Popup opened";
                            b.setClickShortcut(ShortcutAction.KeyCode.ENTER);
                        } else {
                            b.removeClickShortcut();
                        }
                        Notification.show(msg);
                    }
                });
        horizontalLayout.addComponent(listenerButton);
        listenerButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (!listenerButton.isButtonClickTogglesPopupVisibility()) {
                    listenerButton.setPopupVisible(!listenerButton.isPopupVisible());
                }
            }
        });

        PopupButton comboBoxButton = new PopupButton("ComboBox in Popup");
        popupButtons.add(comboBoxButton);
        ComboBox cb = new ComboBox();
        cb.addItem("Item 1");
        cb.addItem("Item 2");
        cb.setInputPrompt("ComboBox");
        comboBoxButton.setContent(cb);
        horizontalLayout.addComponent(comboBoxButton);

        PopupButton tableButton = new PopupButton("Table in Popup");
        popupButtons.add(tableButton);
        Table table = new Table();
        table.setHeight("200px");
        table.addContainerProperty("property1", String.class, "-");
        table.addContainerProperty("property2", String.class, "-");
        table.addItem();
        tableButton.setContent(table);
        horizontalLayout.addComponent(tableButton);


        Button openSubwindowButton = new Button("Open subwindow",
                new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent event) {
                        Window w = new Window();
                        w.center();
                        PopupButton popupButton = new PopupButton();
                        popupButtons.add(popupButton);
                        VerticalLayout l = new VerticalLayout();
                        l.setMargin(true);
                        l.setSpacing(true);
                        l.addComponent(new PopupDateField());
                        l.addComponent(createPopupButton());
                        popupButton.setContent(l);
                        w.setContent(popupButton);
                        getUI().addWindow(w);
                    }
                });
        horizontalLayout.addComponent(openSubwindowButton);

        final PopupButton noContentPopupButton = new PopupButton("No content");
        popupButtons.add(noContentPopupButton);
        horizontalLayout.addComponent(noContentPopupButton);

        Alignment[] aligns = new Alignment[] { Alignment.TOP_RIGHT,
                Alignment.BOTTOM_LEFT, Alignment.BOTTOM_RIGHT };
        for (Alignment align : aligns) {
            PopupButton b2 = createPopupButton();
            addComponent(b2);
            setComponentAlignment(b2, align);
        }
    }

    private PopupButton createPopupButton() {
        PopupButton popupButton = new PopupButton("Add");
        popupButtons.add(popupButton);
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

    private VerticalLayout createDirectionSelector() {
        VerticalLayout layout = new VerticalLayout();

        final NativeSelect directionSelector = new NativeSelect();
        directionSelector.setNullSelectionAllowed(false);
        directionSelector.addItem(Alignment.BOTTOM_RIGHT);
        directionSelector.setItemCaption(Alignment.BOTTOM_RIGHT, "BOTTOM_RIGHT");
        directionSelector.addItem(Alignment.BOTTOM_CENTER);
        directionSelector.setItemCaption(Alignment.BOTTOM_CENTER, "BOTTOM_CENTER");

        directionSelector.addItem(Alignment.BOTTOM_LEFT);
        directionSelector.setItemCaption(Alignment.BOTTOM_LEFT, "BOTTOM_LEFT");

        directionSelector.setValue(Alignment.BOTTOM_RIGHT);
        directionSelector.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                for (PopupButton popupButton : popupButtons) {
                    popupButton.setDirection((Alignment) directionSelector.getValue());
                }
            }
        });
        layout.addComponent(directionSelector);

        final CheckBox buttonClickTogglesPopupVisibility = new CheckBox("Button click toggles popup visibility", true);
        buttonClickTogglesPopupVisibility.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                for (PopupButton popupButton : popupButtons) {
                    popupButton.setButtonClickTogglesPopupVisibility(buttonClickTogglesPopupVisibility.getValue());
                }
            }
        });
        layout.addComponent(buttonClickTogglesPopupVisibility);

        return layout;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
