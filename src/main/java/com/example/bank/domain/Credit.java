package com.example.bank.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Objects;

@Entity
@Table(name = "CREDIT")
@Data
@AllArgsConstructor
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CREDITID")
    private Long creditId;
    @Valid
    @Column(name = "LIMIT")
    private Long limit;
    @Valid
    @Column(name = "INTERESTRATE")
    private Double interestRate;
    @Valid
    @Column(name = "NAME")
    private String name;


    public Credit(Long limit, Double interestRate, String name) {
        this.limit = limit;
        this.interestRate = interestRate;
        this.name = name;
    }

    public Credit() {
        this.limit = 0L;
        this.interestRate = 0.0;
        this.name = "Новый кредит";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Credit credit = (Credit) o;
        return creditId.equals(credit.creditId) && limit.equals(credit.limit) && interestRate.equals(credit.interestRate) && name.equals(credit.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creditId, limit, interestRate, name);
    }
}
