package com.example.bank.repo;

import com.example.bank.domain.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CreditRepo extends JpaRepository<Credit, Long> {
    Credit findByCreditId(Long creditId);
    Credit findByName(String creditName);
    @Query("from Credit e " +
            "where " +
            "   concat(e.name, ' ') like concat('%', :name, '%')")
    List<Credit> findListByName(@Param("name") String name);
}
