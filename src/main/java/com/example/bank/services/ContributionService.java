package com.example.bank.services;

import com.example.bank.domain.Contribution;
import com.example.bank.domain.CreditOffer;

import java.util.List;

public interface ContributionService {
    List<Contribution> findAll();

    void add(Contribution contribution);

    void addAll(List<Contribution> contributions);

    List<Contribution> calculate(CreditOffer creditOffer);

    List<Contribution> findAllByCreditOffer(CreditOffer creditOffer);

    void deleteAll();
}
