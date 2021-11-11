package com.example.bank.services.impl;

import com.example.bank.domain.Contribution;
import com.example.bank.domain.CreditOffer;
import com.example.bank.repo.ContributionRepo;
import com.example.bank.services.ContributionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContributionServiceImpl implements ContributionService {
    private final BigDecimal MONTH_COUNT = new BigDecimal(12);
    private final BigDecimal ONE_HUNDRED_PERCENT = new BigDecimal(100);
    private final ContributionRepo contributionRepo;


    @Override
    public List<Contribution> findAll() {
        return contributionRepo.findAll();
    }

    @Override
    public void add(Contribution contribution) {
        contributionRepo.save(contribution);
    }

    @Override
    public void addAll(List<Contribution> contributions) {
        contributionRepo.saveAll(contributions);
    }

    @Override
    public List<Contribution> calculate(CreditOffer creditOffer) {
        List<Contribution> contributions = new ArrayList<>();
        LocalDate payDay = LocalDate.now();
        BigDecimal interestRateMonth;

        interestRateMonth = creditOffer.getCredit().getInterestRate()
                .setScale(5, RoundingMode.HALF_DOWN)
                .divide(MONTH_COUNT, RoundingMode.HALF_DOWN)
                .divide(ONE_HUNDRED_PERCENT, RoundingMode.HALF_DOWN);

        int countMonth = (int) creditOffer.getMonthCount();
        BigDecimal amount = creditOffer.getAmount().setScale(5, RoundingMode.HALF_DOWN);

        BigDecimal pay = interestRateMonth.multiply(((BigDecimal.ONE.add(interestRateMonth))).pow(countMonth))
                .setScale(5, RoundingMode.HALF_DOWN)
                .divide((((BigDecimal.ONE.add(interestRateMonth)).pow(countMonth)).subtract(BigDecimal.ONE))
                        .setScale(5, RoundingMode.HALF_DOWN), RoundingMode.HALF_DOWN)
                .multiply(amount);

        for (int i = 1; i < countMonth; i++) {
            amount = amount.add(amount.multiply(interestRateMonth)).subtract(pay);
            payDay = payDay.plusMonths(1);
            contributions.add(newContribution(i, creditOffer, amount, pay, payDay, interestRateMonth));
        }
        amount = BigDecimal.ZERO;
        payDay = payDay.plusMonths(1);
        contributions.add(newContribution(countMonth, creditOffer, amount, pay, payDay, interestRateMonth));
        addAll(contributions); //СОХРОНЯЕМ ВСЕ ВЫПЛАТЫ В БД!

        return contributions;
    }
    public Contribution newContribution(int i, CreditOffer creditOffer, BigDecimal amount,  BigDecimal pay,
                                        LocalDate payDay, BigDecimal interestRateMonth){
        BigDecimal percent = amount.multiply(interestRateMonth);
        BigDecimal body = pay.subtract(percent);
        return  new Contribution(
                (long) i,
                creditOffer,
                payDay,
                amount.setScale(2, RoundingMode.HALF_DOWN),
                body.setScale(2, RoundingMode.HALF_DOWN),
                percent.setScale(2, RoundingMode.HALF_DOWN),
                pay.setScale(2, RoundingMode.HALF_DOWN));
    }

    @Override
    public List<Contribution> findAllByCreditOffer(CreditOffer creditOffer) {
        return contributionRepo.findAllByCreditOffer(creditOffer);
    }


    @Override
    public void deleteAll() {
        contributionRepo.deleteAll();
    }
}
