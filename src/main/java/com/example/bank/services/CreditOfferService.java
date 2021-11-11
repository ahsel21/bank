package com.example.bank.services;

import com.example.bank.domain.Client;
import com.example.bank.domain.Credit;
import com.example.bank.domain.CreditOffer;

import java.util.List;

public interface CreditOfferService {
    List<CreditOffer> findAll();

    void deleteByCreditOfferId(long creditOfferId);

    void addCreditOffer(CreditOffer creditOffer);

    List<CreditOffer> findAllByClient(Client client);

    List<CreditOffer> findAllByCredit(Credit credit);

    void delete(CreditOffer creditOffer);
}
