package com.example.bank.services;

import com.example.bank.domain.Credit;

import java.util.List;

public interface CreditService {
    List<Credit> findAll();

    Credit findByName(String creditName);

    List<Credit> findListByName(String name);
}