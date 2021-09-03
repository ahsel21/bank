package com.example.bank.domain;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigInteger;

@Entity
@Table(name = "Client")
@Data
public class Client {
    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Valid
    @Size(min = 10)
    @Column(name = "FULLNAME")
    private String fullName;
    @Valid
    @Size(min = 11)
    @Size(max = 11)
    @Column(name = "PHONENUMBER")
    private String phoneNumber;
    @Valid
    @Email
    @Column(name = "EMAIL")
    private String email;
    @Valid
    @Size(min = 6)
    @Size(max = 6)
    @Column(name = "PASSPORTID")
    private String passportId;

}
