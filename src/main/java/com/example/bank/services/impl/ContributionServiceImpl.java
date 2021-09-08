package com.example.bank.services.impl;

import com.example.bank.domain.Contribution;
import com.example.bank.domain.CreditOffer;
import com.example.bank.repo.ContributionRepo;
import com.example.bank.services.ContributionService;
import org.decimal4j.util.DoubleRounder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContributionServiceImpl implements ContributionService {
    @Autowired
    private ContributionRepo contributionRepo;

    @Override
    public List<Contribution> findAll() {
        return contributionRepo.findAll();
    }


    @Override
    public void add(Contribution contribution) {
        contributionRepo.save(contribution);
    }

    public List<Contribution> calculate(CreditOffer creditOffer){
        double interestRateMonth = creditOffer.getCredit().getInterestRate() / 12 / 100;
        int countMonth = (int) creditOffer.getMonthCount();
        double amount = creditOffer.getAmount();
        double pay = (interestRateMonth * Math.pow((1 + interestRateMonth), countMonth)) / ((Math.pow((1 + interestRateMonth), countMonth)) - 1) * amount;
        double percent = amount * interestRateMonth;
        double body = pay - percent;
        LocalDate payDay = LocalDate.now();
        List<Contribution> contributions = new ArrayList<>();
        for (int i = 1; i < countMonth; i++) {
            percent = amount * interestRateMonth;
            body = pay - percent;
            amount = amount + (amount * interestRateMonth) - pay;
            payDay = payDay.plusMonths(1);
            Contribution contribution = new Contribution((long) i, creditOffer, payDay, DoubleRounder.round(amount, 2),DoubleRounder.round(body, 2) , DoubleRounder.round(percent, 2), DoubleRounder.round(pay, 2));
            contributions.add(contribution);
        }
        percent = amount * interestRateMonth;
        body = pay - percent;
        amount = 0;
        payDay = payDay.plusMonths(1);
        Contribution contribution = new Contribution((long) countMonth, creditOffer, payDay, DoubleRounder.round(amount, 2),DoubleRounder.round(body, 2) , DoubleRounder.round(percent, 2), DoubleRounder.round(pay, 2));
        contributions.add(contribution);
        return contributions;
    }
}
