package com.example.bank.components;

import com.example.bank.domain.Credit;
import com.example.bank.repo.CreditRepo;
import com.example.bank.services.CreditOfferService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Setter;

import java.math.BigDecimal;

@SpringComponent
@UIScope
public class CreditEditor extends VerticalLayout implements KeyNotifier {
    private final CreditRepo creditRepo;
    private final CreditOfferService creditOfferService;
    private final TextField name = new TextField("Имя кредита");
    private final Binder<Credit> binder = new Binder<>(Credit.class);
    private Credit credit;
    @Setter
    private CreditEditor.ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }

    public CreditEditor(CreditRepo creditRepo, CreditOfferService creditOfferService) {
        this.creditRepo = creditRepo;
        this.creditOfferService = creditOfferService;
        Button save = new Button("Save", VaadinIcon.CHECK.create());
        Button cancel = new Button("Cancel");
        Button delete = new Button("Delete", VaadinIcon.TRASH.create());
        HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);
        TextField interestRate1 = new TextField("Процентная ставка");
        TextField limit1 = new TextField("Лимит по кредиту");
        HorizontalLayout fields = new HorizontalLayout(limit1, interestRate1, name);
        add(fields, actions);

        binder.forField(limit1)
                .withConverter(new StringToBigDecimalConverter("Only digits allowed"))
                .withValidator(
                        limit -> limit.compareTo(BigDecimal.ZERO) > 0,
                        "Limit rate must be greater than zero")
                .bind(Credit::getLimit, Credit::setLimit);

        binder.forField(interestRate1)
                .withConverter(new StringToBigDecimalConverter("Only digits allowed"))
                .withValidator(
                        interestRate -> interestRate.compareTo(BigDecimal.ZERO) > 0,
                        "Interest rate must be greater than zero")
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
        if (!creditOfferService.findAllByCredit(credit).isEmpty()) {
            Notification notification = new Notification(
                    "Этот кредит используется в кредитных предложениях!", 5000);
            notification.open();
            return;
        }
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
