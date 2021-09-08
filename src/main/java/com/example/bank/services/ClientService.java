package com.example.bank.services;

import com.example.bank.domain.Client;

import java.util.List;

public interface ClientService {
    List<Client> findAll();
    Client findByPassportId(String passportId);
}
