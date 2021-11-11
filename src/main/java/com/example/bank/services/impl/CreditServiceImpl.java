package com.example.bank.services.impl;

import com.example.bank.domain.Credit;
import com.example.bank.repo.CreditRepo;
import com.example.bank.services.CreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {
    private final CreditRepo creditRepo;

    public List<Credit> findAll() {
        return creditRepo.findAll();
    }

    @Override
    public Credit findByName(String creditName) {
        return creditRepo.findByName(creditName);
    }

    @Override
    public List<Credit> findListByName(String name) {
        return creditRepo.findListByName(name);
    }
}