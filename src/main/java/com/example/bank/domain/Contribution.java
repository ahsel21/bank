package com.example.bank.domain;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;


@Entity
@Table(name = "CONTRIBUTION")
@Data
@NoArgsConstructor
public class Contribution {
    @Id
    @Column(name = "CONTRIBUTIONID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contributionId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CREDITOFFERID")
    private CreditOffer creditOffer;

    @Column(name = "PAYDAY")
    @NotNull
    private LocalDate payDay;

    @Column(name = "AMOUNT")
    @NotNull
    private double amount;

    @Column(name = "BODY")
    @NotNull
    private double body;

    @Column(name = "PERCENT")
    @NotNull
    private double percent;

    @Column(name = "PAY")
    @NotNull
    private double pay;

    public Contribution(Long contributionId, CreditOffer creditOffer, LocalDate payDay, double amount, double body, double percent, double pay) {
        this.contributionId = contributionId;
        this.creditOffer = creditOffer;
        this.payDay = payDay;
        this.amount = amount;
        this.body = body;
        this.percent = percent;
        this.pay = pay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contribution that = (Contribution) o;
        return Double.compare(that.amount, amount) == 0 && Double.compare(that.body, body) == 0 && Double.compare(that.percent, percent) == 0 && Double.compare(that.pay, pay) == 0 && contributionId.equals(that.contributionId) && creditOffer.equals(that.creditOffer) && payDay.equals(that.payDay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contributionId, creditOffer, payDay, amount, body, percent, pay);
    }
}
