package com.example.bank.repo;

import com.example.bank.domain.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepo extends JpaRepository<Bank, Long> {
}
