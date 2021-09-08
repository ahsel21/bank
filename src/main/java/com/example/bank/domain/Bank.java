package com.example.bank.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "BANK")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "BANKID",  unique = true)
    private Integer id;
    @OneToMany(mappedBy = "bank", fetch = FetchType.EAGER)
    private Set<CreditOffer> creditOffers;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return id.equals(bank.id) && creditOffers.equals(bank.creditOffers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creditOffers);
    }
}
