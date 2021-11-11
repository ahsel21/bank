package com.example.bank.components;

import com.example.bank.domain.Client;
import com.example.bank.repo.ClientRepo;
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
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Setter;

@SpringComponent
@UIScope
public class ClientEditor extends VerticalLayout implements KeyNotifier {
    private final ClientRepo clientRepo;
    private final CreditOfferService creditOfferService;
    private final int MIN_NAME_LENGTH = 10;
    private final int MIN_TELEPHONE_LENGTH = 11;
    private final int MAX_TELEPHONE_LENGTH = 13;
    private final int PASSPORT_LENGTH = 6;
    private final TextField fullName = new TextField("Full name");
    private final Binder<Client> binder = new Binder<>(Client.class);
    private Client client;
    @Setter
    private ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }

    public ClientEditor(ClientRepo clientRepo, CreditOfferService creditOfferService) {
        this.clientRepo = clientRepo;
        this.creditOfferService = creditOfferService;
        Button delete = new Button("Delete", VaadinIcon.TRASH.create());
        Button cancel = new Button("Cancel");
        Button save = new Button("Save", VaadinIcon.CHECK.create());
        HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);
        TextField phoneNumberField = new TextField("Phone number");
        TextField emailField = new TextField("Email");
        TextField passportIdField = new TextField("Passport ID");
        HorizontalLayout fields = new HorizontalLayout(fullName, phoneNumberField, emailField, passportIdField);

        add(fields, actions);
        binder.bindInstanceFields(this);

        setSpacing(true);
        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");
        addKeyPressListener(Key.ENTER, e -> save());
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> cancel());

        binder.forField(fullName)
                .withValidator(
                        name -> name.length() >= MIN_NAME_LENGTH,
                        "Name must contain at least 10 characters")
                .bind(Client::getFullName, Client::setFullName);

        binder.forField(phoneNumberField)
                .withValidator(new RegexpValidator("Only digits allowed", "\\d*"))
                .withValidator(
                        phoneNumber -> phoneNumber.length() >= MIN_TELEPHONE_LENGTH,
                        "Phone number must contain from 11 to 13 characters")
                .withValidator(
                        phoneNumber -> phoneNumber.length() <= MAX_TELEPHONE_LENGTH,
                        "Phone number must contain from 11 to 13 characters")
                .bind(Client::getPhoneNumber, Client::setPhoneNumber);

        binder.forField(emailField)
                .withValidator(new EmailValidator(
                        "This doesn't look like a valid email address"))
                .bind(Client::getEmail, Client::setEmail);

        binder.forField(passportIdField)
                .withValidator(new RegexpValidator("Only digits allowed", "\\d*"))
                .withValidator(
                        passportId -> passportId.length() == PASSPORT_LENGTH,
                        "Passport ID must contain 6 characters")
                .bind(Client::getPassportId, Client::setPassportId);

        setVisible(false);
    }

    public void editClient(Client newClient) {
        if (newClient == null) {
            setVisible(false);
            return;
        }
        if (newClient.getClientId() != null) {
            this.client = clientRepo.findById(newClient.getClientId()).orElse(newClient);
        } else {
            this.client = newClient;
        }
        binder.setBean(client);
        setVisible(true);
        fullName.focus();
    }

    private void delete() {
        if (!creditOfferService.findAllByClient(client).isEmpty()) {
            Notification notification = new Notification(
                    "Этот клиент имеет не закрытые кредитные предложения!", 5000);
            notification.open();
            return;
        }
        clientRepo.delete(client);
        changeHandler.onChange();
    }

    private void save() {
        if (binder.validate().isOk()) {
            clientRepo.save(client);
            changeHandler.onChange();
        }
    }
    public void cancel() {
        changeHandler.onChange();
    }
}
