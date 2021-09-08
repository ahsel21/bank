package com.example.bank.services.impl;

import com.example.bank.domain.Client;
import com.example.bank.domain.Credit;
import com.example.bank.domain.CreditOffer;
import com.example.bank.repo.ClientRepo;
import com.example.bank.repo.CreditOfferRepo;
import com.example.bank.repo.CreditRepo;
import com.example.bank.services.CreditOfferService;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditOfferServiceImpl implements CreditOfferService {

    @Autowired
    private CreditOfferRepo creditOfferRepo;
    @Autowired
    private ClientRepo clientRepo;
    @Autowired
    private CreditRepo creditRepo;

    public List<CreditOffer> findAll() {
        return creditOfferRepo.findAll();
    }

    @Override
    public CreditOffer findByCreditOfferId(int creditOfferId) {
        return creditOfferRepo.findByCreditOfferId(creditOfferId);
    }

    @Override
    public void deleteByCreditOfferId(long creditOfferId) {
        creditOfferRepo.deleteById((long) creditOfferId);
    }


    public boolean addCreditOffer(CreditOffer creditOffer) {
        Client client = clientRepo.findByPassportId(creditOffer.getClient().getPassportId());
        Credit credit = creditRepo.findByName(creditOffer.getCredit().getName());
        if (client != null && credit != null ){
            if (creditOffer.getAmount() > credit.getLimit()){
                Notification.show("limit").setPosition(Notification.Position.MIDDLE);
                return false;
            }
            creditOffer.setCredit(credit);
            creditOffer.setClient(client);
            CreditOffer newCreditOffer = new CreditOffer(client,credit, creditOffer.getAmount(), creditOffer.getMonthCount());
            creditOfferRepo.save(newCreditOffer);
            return true;
        } else {
            return false;
        }
    }
}
