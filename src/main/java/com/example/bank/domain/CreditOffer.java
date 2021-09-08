package com.example.bank.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "CREDITOFFER")
@Data
@NoArgsConstructor
public class CreditOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CREDITOFFERID")
    private Long creditOfferId;
    @JoinColumn(name = "CLIENTID")
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Client client;
    @JoinColumn(name = "CREDITID")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @NotNull
    private Credit credit;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BANKID")
    private Bank bank;
    @OneToMany(mappedBy = "creditOffer", fetch = FetchType.EAGER)
    private Set<Contribution> contributions;
    @Column(name = "AMOUNT")
    private long amount;
    @Column(name = "MONTHCOUNT")
    private long monthCount;


    public CreditOffer(Client client, Credit credit, long amount) {
        this.client = client;
        this.credit = credit;
        this.amount = amount;
    }

    public CreditOffer(Client client, Credit credit, long amount, long monthCount) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreditOffer that = (CreditOffer) o;
        return amount == that.amount && monthCount == that.monthCount && creditOfferId.equals(that.creditOfferId) && client.equals(that.client) && credit.equals(that.credit) && bank.equals(that.bank) && contributions.equals(that.contributions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creditOfferId, client, credit, bank, contributions, amount, monthCount);
    }
}
