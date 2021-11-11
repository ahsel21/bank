package com.example.bank.components;

import com.example.bank.domain.Client;
import com.example.bank.domain.Credit;
import com.example.bank.domain.CreditOffer;
import com.example.bank.services.ClientService;
import com.example.bank.services.ContributionService;
import com.example.bank.services.CreditOfferService;
import com.example.bank.services.CreditService;
import com.example.bank.view.CreditOffersList;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.data.converter.StringToLongConverter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class CreditOfferEditor extends VerticalLayout {
    private final CreditOfferService creditOfferService;
    private final CreditService creditService;
    private final ClientService clientService;
    private final ContributionService contributionService;
    private final CreditOffersList creditOffersList;
    private final ComboBox<String> clientPassportLabel = new ComboBox<>("Client passport");
    private final ComboBox<String> creditNameLabel = new ComboBox<>("Credit name");
    private final TextField amount = new TextField("Credit amount");
    private final TextField monthCount = new TextField("Month count");
    private final Binder<CreditOffer> binder = new Binder<>(CreditOffer.class);

    public CreditOfferEditor(CreditOffersList creditOffersList, CreditOfferService creditOfferService,
                             ClientService clientService,
                             CreditService creditService, ContributionService contributionService) {
        this.creditOfferService = creditOfferService;
        this.creditOffersList = creditOffersList;
        this.creditService = creditService;
        this.clientService = clientService;
        this.contributionService = contributionService;
        setVisible(false);

        binder.forField(amount)
                .withConverter(
                        new StringToBigDecimalConverter("Only digits allowed"))
                .withValidator(
                        amount -> amount.compareTo(BigDecimal.ZERO) > 0,
                        "Amount must be greater than zero")
                .bind(CreditOffer::getAmount, CreditOffer::setAmount);

        binder.forField(monthCount)
                .withConverter(
                        new StringToLongConverter("Only digits allowed"))
                .withValidator(
                        monthCount -> monthCount > 0,
                        "Month count must be greater than zero")
                .bind(CreditOffer::getMonthCount, CreditOffer::setMonthCount);

        List<Client> clients = clientService.findAll();
        clientPassportLabel.setItems(clients
                .stream()
                .map(Client::getPassportId)
                .collect(Collectors.toList()));

        List<Credit> credits = creditService.findAll();
        creditNameLabel.setItems(credits
                .stream()
                .map(Credit::getName)
                .collect(Collectors.toList()));

        HorizontalLayout fields = new HorizontalLayout(clientPassportLabel, creditNameLabel, amount, monthCount);
        add(fields);
        Button save = new Button("Save", VaadinIcon.CHECK.create());
        Button cancel = new Button("Cancel");
        Button delete = new Button("Delete", VaadinIcon.TRASH.create());
        HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);
        add(actions);
        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> cancel());
    }

    private void delete() {
        contributionService.deleteAll();
        creditOfferService.deleteByCreditOfferId(binder.getBean().getCreditOfferId());
        UI.getCurrent().getPage().reload();
    }

    public void cancel() {
        creditOffersList.updateList();
        monthCount.setValue("");
        amount.setValue("");
        creditNameLabel.setValue("");
        clientPassportLabel.setValue("");
        setVisible(false);
    }

    private void save() {
        Credit credit = creditService.findByName(creditNameLabel.getValue());
        Client client = clientService.findByPassportId(clientPassportLabel.getValue());
        BigDecimal amount = new BigDecimal(this.amount.getValue());
        long count = Long.parseLong(monthCount.getValue());
        client.setPassportId(clientPassportLabel.getValue());
        CreditOffer creditOffer = new CreditOffer(client, credit, amount, count);
        creditOfferService.addCreditOffer(creditOffer);
        cancel();
    }

    public void set(CreditOffer creditOffer) {
        binder.setBean(creditOffer);
        clientPassportLabel.setValue(creditOffer.getClient().getPassportId());
        creditNameLabel.setValue(creditOffer.getCredit().getName());
        amount.setValue(String.valueOf(creditOffer.getAmount()));
        monthCount.setValue(String.valueOf(creditOffer.getMonthCount()));
        setVisible(true);
    }
}
