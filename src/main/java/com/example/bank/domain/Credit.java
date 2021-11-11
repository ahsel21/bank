package com.example.bank.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import java.math.BigDecimal;

@Entity
@Table(name = "CREDIT")
@Data
@AllArgsConstructor
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CREDIT_ID")
    private Long creditId;

    @Valid
    @Column(name = "LIMIT")
    private BigDecimal limit;

    @Valid
    @Column(name = "INTEREST_RATE")
    private BigDecimal interestRate;

    @Valid
    @Column(name = "NAME")
    private String name;

    public Credit() {
        this(BigDecimal.ZERO, BigDecimal.ZERO, "Новый кредит");
    }

    public Credit(BigDecimal limit, BigDecimal interestRate, String name) {
        this.limit = limit;
        this.interestRate = interestRate;
        this.name = name;
    }
}
