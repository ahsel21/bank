package com.example.bank.repo;

import com.example.bank.domain.Credit;
import com.example.bank.domain.CreditOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CreditOfferRepo extends JpaRepository<CreditOffer, Long> {
    List<CreditOffer> findAll();

    CreditOffer findByCreditOfferId(int creditOfferId);
}
