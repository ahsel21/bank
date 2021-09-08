package com.example.bank.components;

import com.example.bank.domain.Client;
import com.example.bank.domain.Credit;
import com.example.bank.domain.CreditOffer;
import com.example.bank.services.impl.ClientServiceImpl;
import com.example.bank.services.impl.ContributionServiceImpl;
import com.example.bank.services.impl.CreditOfferServiceImpl;
import com.example.bank.services.impl.CreditServiceImpl;
import com.example.bank.view.CreditOffeList;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToLongConverter;

import java.util.List;
import java.util.stream.Collectors;


public class CreditOfferEditor extends VerticalLayout {

    private CreditOfferServiceImpl creditOfferService;
    private CreditServiceImpl creditService;
    private ClientServiceImpl clientService;
    private CreditOffeList creditOffeList;
    private ComboBox<String> clientPassportLabel = new ComboBox<>("Client passport");
    private ComboBox<String> creditNameLabel = new ComboBox<>("Credit name");
    private TextField amount = new TextField("Credit amount");
    private TextField monthCount = new TextField("Month count");
    private HorizontalLayout fields = new HorizontalLayout(clientPassportLabel, creditNameLabel, amount, monthCount);
    private Button save = new Button("Save", VaadinIcon.CHECK.create());
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    private HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    private Binder<CreditOffer> binder = new Binder<>(CreditOffer.class);

    public CreditOfferEditor(CreditOffeList creditOffeList, CreditOfferServiceImpl creditOfferService, ContributionServiceImpl contributionService, ClientServiceImpl clientService,
                             CreditServiceImpl creditService) {
        this.creditOfferService = creditOfferService;
        this.creditOffeList = creditOffeList;
        this.creditService = creditService;
        this.clientService = clientService;
        setVisible(false);
        binder.forField(amount)
                .withConverter(new StringToLongConverter("Only digits allowed"))
                .bind(CreditOffer::getAmount, CreditOffer::setAmount);
        binder.forField(monthCount)
                .withConverter(new StringToLongConverter("Only digits allowed"))
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
        add(fields);
        add(actions);
        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> cancel());

    }

    private void delete() {
        CreditOffer creditOffer = binder.getBean();
        creditOfferService.deleteByCreditOfferId(creditOffer.getCreditOfferId());
        cancel();
    }

    public void cancel() {
        creditOffeList.updateList();
        monthCount.setValue("");
        amount.setValue("");
        creditNameLabel.setValue("");
        clientPassportLabel.setValue("");
        setVisible(false);
    }

    private void save() {
        Credit credit = creditService.findByName(creditNameLabel.getValue());
        Client client = clientService.findByPassportId(clientPassportLabel.getValue());
        long amount = Long.parseLong(this.amount.getValue());
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
