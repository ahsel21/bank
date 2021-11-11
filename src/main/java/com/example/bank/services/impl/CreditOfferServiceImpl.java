package com.example.bank.services.impl;

import com.example.bank.domain.Client;
import com.example.bank.domain.Credit;
import com.example.bank.domain.CreditOffer;
import com.example.bank.repo.ClientRepo;
import com.example.bank.repo.CreditOfferRepo;
import com.example.bank.repo.CreditRepo;
import com.example.bank.services.CreditOfferService;
import com.vaadin.flow.component.notification.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditOfferServiceImpl implements CreditOfferService {
    private final CreditOfferRepo creditOfferRepo;
    private final ClientRepo clientRepo;
    private final CreditRepo creditRepo;

    public List<CreditOffer> findAll() {
        return creditOfferRepo.findAll();
    }

    @Override
    public void deleteByCreditOfferId(long creditOfferId) {
        creditOfferRepo.deleteById(creditOfferId);
    }

    @Override
    public void addCreditOffer(CreditOffer creditOffer) {
        Client client = clientRepo.findByPassportId(creditOffer.getClient().getPassportId());
        Credit credit = creditRepo.findByName(creditOffer.getCredit().getName());
        if (client != null && credit != null) {
            if (creditOffer.getAmount().compareTo(credit.getLimit()) > 0) {
                Notification.show("limit").setPosition(Notification.Position.MIDDLE);
                return;
            }
            creditOffer.setCredit(credit);
            creditOffer.setClient(client);
            CreditOffer newCreditOffer = new CreditOffer(client,
                    credit,
                    creditOffer.getAmount(),
                    creditOffer.getMonthCount());
            creditOfferRepo.save(newCreditOffer);
        }
    }

    @Override
    public List<CreditOffer> findAllByClient(Client client) {
        return creditOfferRepo.findAllByClient(client);
    }

    @Override
    public List<CreditOffer> findAllByCredit(Credit credit) {
        return creditOfferRepo.findAllByCredit(credit);
    }

    @Override
    public void delete(CreditOffer creditOffer) {
        creditOfferRepo.delete(creditOffer);
    }
}
