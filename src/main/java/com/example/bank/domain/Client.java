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
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotNull
    @Size(min = 10)
    private String fullName;
    @NotNull
    @Size(min = 11)
    @Size(max = 11)
    private String phoneNumber;
    @NotNull
    @Email
    private String email;
    @NotNull
    @Size(min = 6)
    @Size(max = 6)
    private String passportId;

}
