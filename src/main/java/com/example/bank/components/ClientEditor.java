package com.example.bank.components;

import com.example.bank.domain.Client;
import com.example.bank.repo.ClientRepo;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class ClientEditor extends VerticalLayout implements KeyNotifier {
    private final ClientRepo clientRepo;

    private Client client;

    TextField fullName = new TextField("Full name");
    TextField phoneNumber = new TextField("phoneNumber");
    TextField email = new TextField("email");
    TextField passportId = new TextField("Passport ID");

    private Button save = new Button("Save", VaadinIcon.CHECK.create());
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    private HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<Client> binder = new Binder<>(Client.class);
    @Setter
    private ChangeHandler changeHandler;
    public interface ChangeHandler {
        void onChange();
    }

    @Autowired
    public ClientEditor(ClientRepo clientRepo) {
        this.clientRepo = clientRepo;
        add(fullName, phoneNumber, email, passportId, actions);

        binder.bindInstanceFields(this);
        setSpacing(true);

        binder.forField(fullName)
                .withValidator(
                        name -> name.length() >= 10,
                        "Name must contain at least 10 characters")
                .bind(Client::getFullName, Client::setFullName);

        binder.forField(phoneNumber)
                .withValidator(new RegexpValidator("Only 1-9 allowed","\\d*"))
                .withValidator(
                        phoneNumber -> phoneNumber.length() == 11,
                        "Phone number must contain at 11 characters")
                .bind(Client::getPhoneNumber, Client::setPhoneNumber);


        binder.forField(email)
                .withValidator(new EmailValidator(
                        "This doesn't look like a valid email address"))
                .bind(Client::getEmail, Client::setEmail);
        binder.forField(passportId)
                .withValidator(
                        passportId -> passportId.length() == 6,
                        "Passport ID must contain 6 characters")
                .bind(Client::getPassportId, Client::setPassportId);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editClient(client));
        setVisible(false);
    }

    public void editClient(Client newClient) {
        if (newClient == null) {
            setVisible(false);
            return;
        }
        if (newClient.getId() != null) {
            this.client = clientRepo.findById(newClient.getId()).orElse(newClient);
        } else {
            this.client = newClient;
        }
        binder.setBean(client);
        setVisible(true);
        fullName.focus();
    }

    private void delete() {
        clientRepo.delete(client);
        changeHandler.onChange();
    }

    private void save() {
        if (binder.validate().isOk()) {
            clientRepo.save(client);
            changeHandler.onChange();
        }
    }
}
