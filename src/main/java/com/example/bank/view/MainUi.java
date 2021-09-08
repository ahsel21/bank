package com.example.bank.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;

@Route("/")

public class MainUi extends AppLayout implements RouterLayout {
    public MainUi() {

        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("Банк");
        title.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");
        Tab tabClient = createMenuItem("Клиенты", ClientList.class);
        Tab tabCredits = createMenuItem("Кредиты", CreditList.class);
        Tab tabOffer = createMenuItem("Кредитные приложения", CreditOffeList.class);
        Tabs tabs = new Tabs(tabClient, tabCredits, tabOffer);
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        addToDrawer(tabs);
        addToNavbar(toggle, title);
    }

    private Tab createMenuItem(String title, Class<? extends Component> target) {
        RouterLink link = new RouterLink(null, target);
        link.add(title);
        Tab tab = new Tab();
        tab.add(link);
        return tab;
    }

}
