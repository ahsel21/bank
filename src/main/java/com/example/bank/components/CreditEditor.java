package com.example.bank.components;

import com.example.bank.domain.Credit;
import com.example.bank.repo.CreditRepo;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToDoubleConverter;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class CreditEditor extends VerticalLayout implements KeyNotifier {

    private final CreditRepo creditRepo;
    private Credit credit;
    private TextField limit = new TextField("Лимит по кредиту");
    private TextField interestRate = new TextField("Процентная ставка");
    private TextField name = new TextField("Имя кредита");


    private Button save = new Button("Save", VaadinIcon.CHECK.create());
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    private HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);
    private HorizontalLayout fields = new HorizontalLayout(limit, interestRate, name);
    private Binder<Credit> binder = new Binder<>(Credit.class);
    @Setter
    private CreditEditor.ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }


    @Autowired
    public CreditEditor(CreditRepo creditRepo) {
        this.creditRepo = creditRepo;
        add(fields, actions);

        binder.forField(limit)
                .withConverter(new StringToLongConverter("Only digits allowed"))
                .bind(Credit::getLimit, Credit::setLimit);

        binder.forField(interestRate)
                .withConverter(new StringToDoubleConverter("Only digits allowed"))
                .bind(Credit::getInterestRate, Credit::setInterestRate);

        binder.forField(name)
                .bind(Credit::getName, Credit::setName);
        binder.bindInstanceFields(this);
        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        setSpacing(true);
        addKeyPressListener(Key.ENTER, e -> save());
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> setVisible(false));
        setVisible(false);
    }

    public void editCredit(Credit newCredit) {
        if (newCredit == null) {
            setVisible(false);
            return;
        }
        if (newCredit.getCreditId() != null) {
            this.credit = creditRepo.findById(newCredit.getCreditId()).orElse(newCredit);
        } else {
            this.credit = newCredit;
        }
        binder.setBean(credit);
        setVisible(true);
        name.focus();
    }

    private void delete() {
        creditRepo.delete(credit);
        changeHandler.onChange();
    }

    private void save() {
        if (binder.validate().isOk()) {
            creditRepo.save(credit);
            changeHandler.onChange();
        }
    }
}
