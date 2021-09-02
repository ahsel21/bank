package com.example.bank.repo;

import com.example.bank.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientRepo extends JpaRepository<Client, Integer> {

    @Query("from Client e " +
            "where " +
            "   concat(e.fullName, ' ', e.email) like concat('%', :name, '%')")
    List<Client> findByName(@Param("name") String name);
}
