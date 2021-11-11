package com.example.bank.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Entity
@Table(name = "CLIENT")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CLIENT_ID")
    private Long clientId;

    @Size(min = 10)
    @Column(name = "FULL_NAME")
    private String fullName;

    @Size(min = 11, max = 13)
    @Column(name = "PHONE_NUMBER", unique = true)
    private String phoneNumber;

    @Email
    @Column(name = "EMAIL", unique = true)
    private String email;

    @Size(min = 6, max = 6)
    @Column(name = "PASSPORT_ID", unique = true)
    private String passportId;
}
