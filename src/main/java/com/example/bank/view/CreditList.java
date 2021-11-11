package com.example.bank.view;

import com.example.bank.components.CreditEditor;
import com.example.bank.domain.Credit;
import com.example.bank.services.CreditService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

@Route(value = "/credits", layout = MainUi.class)
public class CreditList extends VerticalLayout {
    private final CreditService creditService;
    private final CreditEditor creditEditor;
    private final Grid<Credit> creditGrid = new Grid<>(Credit.class);
    private final TextField filter = new TextField("");

    public CreditList(CreditService creditService, CreditEditor editor) {
        this.creditService = creditService;
        this.creditEditor = editor;
        creditGrid.setItems(creditService.findAll());
        Button addNewBtn = new Button("Добавить кредит");
        HorizontalLayout toolbar = new HorizontalLayout(filter, addNewBtn);
        add(new Label("Список кредитов:"), toolbar, creditGrid, editor);
        filter.setPlaceholder("Поиск...");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(field -> fillList(field.getValue()));

        creditGrid
                .asSingleSelect()
                .addValueChangeListener(e -> creditEditor.editCredit(e.getValue()));
        addNewBtn.addClickListener(e -> creditEditor.editCredit(new Credit()));

        creditEditor.setChangeHandler(() -> {
            creditEditor.setVisible(false);
            fillList(filter.getValue());
        });

        creditGrid.setColumns("name", "limit", "interestRate");
        fillList("");
    }

    private void fillList(String name) {
        if (name.isEmpty()) {
            creditGrid.setItems(this.creditService.findAll());
        } else {
            creditGrid.setItems(this.creditService.findListByName(name));
        }
    }
}
