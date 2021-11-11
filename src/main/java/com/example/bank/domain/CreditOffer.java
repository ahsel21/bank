package com.example.bank.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "CREDIT_OFFER")
@Data
@NoArgsConstructor
public class CreditOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CREDIT_OFFER_ID")
    private Long creditOfferId;

    @JoinColumn(name = "CLIENT_ID")
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Client client;

    @JoinColumn(name = "CREDIT_ID")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @NotNull
    private Credit credit;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BANK_ID")
    private Bank bank;

    @OneToMany(mappedBy = "creditOffer", fetch = FetchType.EAGER)
    private Set<Contribution> contributions;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "MONTH_COUNT")
    private long monthCount;

    public CreditOffer(Client client, Credit credit, BigDecimal amount) {
        this.client = client;
        this.credit = credit;
        this.amount = amount;
    }

    public CreditOffer(Client client, Credit credit, BigDecimal amount, long monthCount) {
        this.client = client;
        this.credit = credit;
        this.amount = amount;
        this.monthCount = monthCount;
    }

    public String getCreditName() {
        return credit.getName();
    }

    public String getClientName() {
        return client.getFullName();
    }
}