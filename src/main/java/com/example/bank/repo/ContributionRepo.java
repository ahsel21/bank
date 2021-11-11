package com.example.bank.repo;

import com.example.bank.domain.Contribution;
import com.example.bank.domain.CreditOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContributionRepo extends JpaRepository<Contribution, Long> {
    void deleteAll();

    List<Contribution> findAllByCreditOffer(CreditOffer creditOffer);
}
