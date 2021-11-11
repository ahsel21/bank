package com.example.bank.services.impl;

import com.example.bank.domain.Client;
import com.example.bank.repo.ClientRepo;
import com.example.bank.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepo clientRepo;

    @Override
    public List<Client> findAll() {
        return clientRepo.findAll();
    }

    @Override
    public Client findByPassportId(String passportId) {
        return clientRepo.findByPassportId(passportId);
    }

    @Override
    public List<Client> findByName(String name) {
        return clientRepo.findByName(name);
    }
}
