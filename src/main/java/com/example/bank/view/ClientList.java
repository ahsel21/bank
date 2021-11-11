package com.example.bank.view;

import com.example.bank.components.ClientEditor;
import com.example.bank.domain.Client;
import com.example.bank.services.ClientService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

@Route(value ="/clients", layout = MainUi.class)
public class ClientList extends VerticalLayout {
    private final ClientService clientService;
    private final ClientEditor clientEditor;
    private final Grid<Client> clientGrid = new Grid<>(Client.class);
    private final TextField filter = new TextField("");

    public ClientList(ClientService clientService, ClientEditor editor) {
        this.clientService = clientService;
        this.clientEditor = editor;
        clientGrid.setItems(clientService.findAll());
        Button addNewBtn = new Button("Добавить клиента");
        HorizontalLayout toolbar = new HorizontalLayout(filter, addNewBtn);
        add(new Label("Список клиентов:"), toolbar, clientGrid, editor);

        filter.setPlaceholder("Поиск...");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(field -> fillList(field.getValue()));
        clientGrid
                .asSingleSelect()
                .addValueChangeListener(e -> clientEditor.editClient(e.getValue()));

        addNewBtn.addClickListener(e -> clientEditor.editClient(new Client()));
        clientGrid.setColumns("fullName", "passportId", "email", "phoneNumber");
        clientEditor.setChangeHandler(() -> {
            clientEditor.setVisible(false);
            fillList(filter.getValue());
        });
        fillList("");
    }

    private void fillList(String name) {
        if (name.isEmpty()) {
            clientGrid.setItems(clientService.findAll());
        } else {
            clientGrid.setItems(clientService.findByName(name));
        }
    }
}