package com.example.bank.repo;

import com.example.bank.domain.Contribution;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ContributionRepo extends JpaRepository<Contribution, Long> {
    void deleteAll();

}
