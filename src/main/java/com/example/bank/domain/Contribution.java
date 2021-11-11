package com.example.bank.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "CONTRIBUTION")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contribution {
    @Id
    @Column(name = "CONTRIBUTION_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contributionId;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CREDIT_OFFER_ID")
    private CreditOffer creditOffer;

    @Column(name = "PAY_DAY")
    @NotNull
    private LocalDate payDay;

    @Column(name = "AMOUNT")
    @NotNull
    private BigDecimal amount;

    @Column(name = "BODY")
    @NotNull
    private BigDecimal body;

    @Column(name = "PERCENT")
    @NotNull
    private BigDecimal percent;

    @Column(name = "PAY")
    @NotNull
    private BigDecimal pay;
}
