package com.example.bank.components;

import com.example.bank.domain.Client;
import com.example.bank.repo.ClientRepo;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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

    private TextField phoneNumber = new TextField("phoneNumber");
    private TextField fullName = new TextField("Full name");
    private TextField email = new TextField("email");
    private TextField passportId = new TextField("Passport ID");

    private Button save = new Button("Save", VaadinIcon.CHECK.create());
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    private HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);
    private HorizontalLayout fields = new HorizontalLayout(fullName, phoneNumber, email, passportId);

    private Binder<Client> binder = new Binder<>(Client.class);
    @Setter
    private ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }

    @Autowired
    public ClientEditor(ClientRepo clientRepo) {
        this.clientRepo = clientRepo;
        add(fields, actions);
        binder.bindInstanceFields(this);
        setSpacing(true);

        binder.forField(fullName)
                .withValidator(
                        name -> name.length() >= 10,
                        "Name must contain at least 10 characters")
                .bind(Client::getFullName, Client::setFullName);

        binder.forField(phoneNumber)
                .withValidator(new RegexpValidator("Only digits allowed", "\\d*"))
                .withValidator(
                        phoneNumber -> phoneNumber.length() == 11,
                        "Phone number must contain at 11 characters")
                .bind(Client::getPhoneNumber, Client::setPhoneNumber);

        binder.forField(email)
                .withValidator(new EmailValidator(
                        "This doesn't look like a valid email address"))
                .bind(Client::getEmail, Client::setEmail);

        binder.forField(passportId)
                .withValidator(new RegexpValidator("Only digits allowed", "\\d*"))
                .withValidator(
                        passportId -> passportId.length() == 6,
                        "Passport ID must contain 6 characters")
                .bind(Client::getPassportId, Client::setPassportId);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");
        addKeyPressListener(Key.ENTER, e -> save());
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> setVisible(false));
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
