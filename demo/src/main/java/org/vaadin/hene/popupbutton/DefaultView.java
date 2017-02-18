package org.vaadin.hene.popupbutton;

import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Resource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Henri Kerola / Vaadin
 */
public class DefaultView extends GridLayout implements View {

    private List<PopupButton> popupButtons = new ArrayList<>();

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
        textArea.addValueChangeListener(event -> Notification.show(event.getValue()));
        textCaptionButton.addPopupVisibilityListener(event -> {
            if (event.isPopupVisible()) {
                textArea.focus();
            }
        });
        textArea.setRows(10);
        textCaptionButton.setContent(textArea);
        horizontalLayout.addComponent(textCaptionButton);

        PopupButton iconButton = new PopupButton();
        popupButtons.add(iconButton);
        iconButton.setIcon(VaadinIcons.USERS);
        iconButton.setStyleName("style1 style2");
        horizontalLayout.addComponent(iconButton);

        VerticalLayout userLayout = new VerticalLayout();
        iconButton.setContent(userLayout);

        Button addUser = new Button("Add user");
        addUser.setStyleName(ValoTheme.BUTTON_LINK);
        addUser.setIcon(VaadinIcons.USER);
        userLayout.addComponent(addUser);
        userLayout.setComponentAlignment(addUser, Alignment.MIDDLE_LEFT);

        Button removeUser = new Button("Remove user");
        removeUser.setStyleName(ValoTheme.BUTTON_LINK);
        removeUser.setIcon(VaadinIcons.CLOSE);
        userLayout.addComponent(removeUser);
        userLayout.setComponentAlignment(removeUser, Alignment.MIDDLE_LEFT);

        final PopupButton listenerButton = new PopupButton();
        popupButtons.add(listenerButton);
        final Button b = new Button("Click me!", event -> {
            listenerButton.setPopupVisible(false);
            Notification.show("Button clicked!");
        });
        listenerButton.setContent(b);
        listenerButton.setCaption("VisibilityListener");
        listenerButton.addPopupVisibilityListener(event -> {
            String msg = "Popup closed";
                if (event.getPopupButton().isPopupVisible()) {
                    msg = "Popup opened";
                    b.setClickShortcut(ShortcutAction.KeyCode.ENTER);
                } else {
                    b.removeClickShortcut();
                }
                Notification.show(msg);
            });
        horizontalLayout.addComponent(listenerButton);
        listenerButton.addClickListener(event -> {
            if (!listenerButton.isButtonClickTogglesPopupVisibility()) {
                listenerButton.setPopupVisible(!listenerButton.isPopupVisible());
            }
        });

        PopupButton comboBoxButton = new PopupButton("ComboBox in Popup");
        popupButtons.add(comboBoxButton);
        ComboBox cb = new ComboBox();
        cb.setItems("Item 1", "Item 2");
        cb.setPlaceholder("ComboBox");
        comboBoxButton.setContent(cb);
        horizontalLayout.addComponent(comboBoxButton);

        PopupButton tableButton = new PopupButton("Grid in Popup");
        popupButtons.add(tableButton);
        Grid grid = new Grid();
        grid.setHeight("200px");
        grid.addColumn(o -> "").setCaption("property1");
        grid.addColumn(o -> "").setCaption("property2");
        grid.setItems("Item 1");
        tableButton.setContent(grid);
        horizontalLayout.addComponent(tableButton);


        Button openSubwindowButton = new Button("Open subwindow", event -> {
            Window w = new Window("Subwindow");
            w.center();
            PopupButton popupButton = new PopupButton();
            popupButtons.add(popupButton);
            VerticalLayout l = new VerticalLayout();
            l.setMargin(true);
            l.setSpacing(true);
            l.addComponent(new DateField());
            l.addComponent(createPopupButton());
            popupButton.setContent(l);
            w.setContent(popupButton);
            getUI().addWindow(w);
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
        popupButton.setIcon(VaadinIcons.PLUS_CIRCLE_O);

        GridLayout gl = new GridLayout(3, 3);
        gl.addComponent(createIconButton(VaadinIcons.FILE_CODE));
        gl.addComponent(createIconButton(VaadinIcons.FILE_FONT));
        gl.addComponent(createIconButton(VaadinIcons.FILE_MOVIE));
        gl.addComponent(createIconButton(VaadinIcons.FILE_PICTURE));
        gl.addComponent(createIconButton(VaadinIcons.FILE_PRESENTATION));
        gl.addComponent(createIconButton(VaadinIcons.FILE_TABLE));
        gl.addComponent(createIconButton(VaadinIcons.FILE_TEXT));
        gl.addComponent(createIconButton(VaadinIcons.FILE_SOUND));
        gl.addComponent(createIconButton(VaadinIcons.FILE_PROCESS));

        popupButton.setContent(gl);
        return popupButton;
    }

    private Button createIconButton(Resource icon) {
        Button b = new Button();
        b.setIcon(icon);
        b.setStyleName(ValoTheme.BUTTON_LINK);
        return b;
    }

    private VerticalLayout createDirectionSelector() {
        VerticalLayout layout = new VerticalLayout();

        final NativeSelect<Alignment> directionSelector = new NativeSelect<>();
        directionSelector.setItems(Alignment.BOTTOM_RIGHT, Alignment.BOTTOM_CENTER, Alignment.BOTTOM_LEFT);
        directionSelector.setItemCaptionGenerator(item -> {
            if (item == Alignment.BOTTOM_RIGHT) {
                return "BOTTOM_RIGHT";
            } else if (item == Alignment.BOTTOM_CENTER) {
                return "BOTTOM_CENTER";
            } else if (item == Alignment.BOTTOM_LEFT) {
                return "BOTTOM_LEFT";
            }
            return "UNKNOWN";
        });
        directionSelector.setValue(Alignment.BOTTOM_RIGHT);
        directionSelector.addValueChangeListener(event -> {
            for (PopupButton popupButton : popupButtons) {
                popupButton.setDirection(directionSelector.getValue());
            }
        });
        layout.addComponent(directionSelector);

        final CheckBox buttonClickTogglesPopupVisibility = new CheckBox("Button click toggles popup visibility", true);
        buttonClickTogglesPopupVisibility.addValueChangeListener(event -> {
            for (PopupButton popupButton : popupButtons) {
                popupButton.setButtonClickTogglesPopupVisibility(buttonClickTogglesPopupVisibility.getValue());
            }
        });
        layout.addComponent(buttonClickTogglesPopupVisibility);

        return layout;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
