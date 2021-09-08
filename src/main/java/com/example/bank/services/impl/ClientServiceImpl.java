package com.example.bank.services.impl;

import com.example.bank.domain.Client;
import com.example.bank.repo.ClientRepo;
import com.example.bank.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientRepo clientRepo;

    public ClientServiceImpl(ClientRepo clientRepo) {
        this.clientRepo = clientRepo;
    }

    public List<Client> findAll(){
        return clientRepo.findAll();
    }

    @Override
    public Client findByPassportId(String passportId) {
        return     clientRepo.findByPassportId(passportId);
    }


}
