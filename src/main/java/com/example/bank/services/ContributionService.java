package com.example.bank.services;

import com.example.bank.domain.Contribution;

import java.util.List;

public interface ContributionService {
    List<Contribution> findAll();
    void add(Contribution contribution);
}
