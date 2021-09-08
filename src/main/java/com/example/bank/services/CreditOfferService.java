package com.example.bank.services;

import com.example.bank.domain.CreditOffer;

import java.util.List;

public interface CreditOfferService {
    List<CreditOffer> findAll();
    CreditOffer findByCreditOfferId(int creditOfferId);
    void deleteByCreditOfferId(long creditOfferId);

}
