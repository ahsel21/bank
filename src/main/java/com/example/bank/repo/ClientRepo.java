package com.example.bank.repo;

import com.example.bank.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClientRepo extends JpaRepository<Client, Long> {

    @Query("from Client e " +
            "where " +
            "   concat(e.fullName, ' ', e.email) like concat('%', :name, '%')")
    List<Client> findByName(@Param("name") String name);

    Optional<Client> findById(Long id);
    Client findByPassportId(String passportId);


}
