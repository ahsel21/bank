package com.example.bank.view;

import com.example.bank.components.ClientEditor;
import com.example.bank.domain.Client;
import com.example.bank.repo.ClientRepo;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value ="/clients", layout = MainUi.class)
public class ClientList extends VerticalLayout {

    private final ClientRepo clientRepo;
    private final ClientEditor clientEditor;
    private Grid<Client> clientGrid = new Grid<>(Client.class);
    private final TextField filter = new TextField("");
    private final Button addNewBtn = new Button("Добавить клиента");
    private HorizontalLayout toolbar = new HorizontalLayout(filter, addNewBtn);

    @Autowired
    public ClientList(ClientRepo clientRepo, ClientEditor editor) {
        this.clientRepo = clientRepo;
        this.clientEditor = editor;
        clientGrid.setItems(clientRepo.findAll());
        add(new Label("Список клиентов:"), toolbar, clientGrid, editor);

        filter.setPlaceholder("Поиск...");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(field -> fillList(field.getValue()));

        clientGrid
                .asSingleSelect()
                .addValueChangeListener(e -> clientEditor.editClient(e.getValue()));

        addNewBtn.addClickListener(e -> clientEditor.editClient(new Client()));

        clientEditor.setChangeHandler(() -> {
            clientEditor.setVisible(false);
            fillList(filter.getValue());
        });

        fillList("");

    }

    private void fillList(String name) {
        if (name.isEmpty()) {
            clientGrid.setItems(this.clientRepo.findAll());
        } else {
            clientGrid.setItems(this.clientRepo.findByName(name));
        }
    }


}