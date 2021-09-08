package com.example.bank.services.impl;

import com.example.bank.domain.Credit;
import com.example.bank.domain.CreditOffer;
import com.example.bank.repo.CreditOfferRepo;
import com.example.bank.repo.CreditRepo;
import com.example.bank.services.CreditService;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class CreditServiceImpl implements CreditService {

    @Autowired
    private CreditRepo creditRepo;

    @Autowired
    private CreditOfferRepo creditOfferRepo;

    public CreditServiceImpl(CreditRepo creditRepo) {
        this.creditRepo = creditRepo;
    }


    public List<Credit> findAll() {
        return creditRepo.findAll();
    }

    @Override
    public Credit findByName(String creditName) {
        return creditRepo.findByName(creditName);
    }


}

