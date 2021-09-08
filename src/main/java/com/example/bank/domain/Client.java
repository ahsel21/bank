package com.example.bank.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Objects;


@Entity
@Table(name = "CLIENT")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    @Id
    @Valid
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CLIENTID",  unique = true)
    private Long clientId;
    @Valid
    @Size(min = 10)
    @Column(name = "FULLNAME")
    private String fullName;
    @Valid
    @Size(min = 11)
    @Size(max = 11)
    @Column(name = "PHONENUMBER",  unique = true)
    private String phoneNumber;
    @Valid
    @Email
    @Column(name = "EMAIL",  unique = true)
    private String email;
    @Valid
    @Size(min = 6)
    @Size(max = 6)
    @Column(name = "PASSPORTID",  unique = true)
    private String passportId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return clientId.equals(client.clientId) && fullName.equals(client.fullName) && phoneNumber.equals(client.phoneNumber) && email.equals(client.email) && passportId.equals(client.passportId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, fullName, phoneNumber, email, passportId);
    }
}
