package com.api.ms_transaction.model;

import jakarta.persistence.*;
import lombok.*;

import javax.print.attribute.standard.DateTimeAtCreation;
import java.math.BigDecimal;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigDecimal id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToOne(mappedBy = "accountDetail", cascade = CascadeType.ALL)
    private CurrencyAccount accountCurrency;

    private BigDecimal balance;
    private Date createAt;
    private Boolean isEnabled;
    private String createdBy;
    private Date lastUpdateAt;

    @OneToMany(mappedBy = "sourceAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Transaction> outgoingTransactions;

    @OneToMany(mappedBy = "destinationAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Transaction> incomingTransactions;



}
