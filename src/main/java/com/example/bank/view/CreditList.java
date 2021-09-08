package com.example.bank.view;

import com.example.bank.components.CreditEditor;
import com.example.bank.domain.Credit;
import com.example.bank.repo.CreditRepo;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "/credits", layout = MainUi.class)
public class CreditList extends VerticalLayout {
    private final CreditRepo creditRepo;
    private final CreditEditor creditEditor;
    private Grid<Credit> creditGrid = new Grid<>(Credit.class);

    private final TextField filter = new TextField("");
    private final Button addNewBtn = new Button("Добавить кредит");
    private HorizontalLayout toolbar = new HorizontalLayout(filter, addNewBtn);

    @Autowired
    public CreditList(CreditRepo creditRepo, CreditEditor editor) {
        this.creditRepo = creditRepo;
        this.creditEditor = editor;
        creditGrid.setItems(creditRepo.findAll());
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
        fillList("");
    }

    private void fillList(String name) {
        if (name.isEmpty()) {
            creditGrid.setItems(this.creditRepo.findAll());
        } else {
            creditGrid.setItems(this.creditRepo.findListByName(name));
        }
    }
}
