package com.api.ms_transaction.model;

import jakarta.persistence.*;
import lombok.*;

import javax.print.attribute.standard.DateTimeAtCreation;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;
import java.util.Set;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountDetail {

    @Id
    private BigDecimal id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Account account;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private CurrencyAccount accountCurrency;

    private BigDecimal balance;
    private DateTimeAtCreation createAt;
    private Boolean isEnabled;
    private String createdBy;
    private Date lastUpdateAt;

    @OneToMany(mappedBy = "sourceAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Transaction> outgoingTransactions;

    @OneToMany(mappedBy = "destinationAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Transaction> incomingTransactions;



}
