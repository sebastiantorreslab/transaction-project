package com.api.ms_transaction.model;


import jakarta.persistence.*;
import lombok.*;

import javax.print.attribute.standard.DateTimeAtCreation;
import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String currencyOrigin;
    private String currencyDestination;
    private BigDecimal amount;
    private DateTimeAtCreation createAt;
    private String status;
    private String createdBy;
    private String transactionType; // todo: possible candidate for strategy pattern

    @ManyToOne(fetch = FetchType.LAZY)
    private AccountDetail sourceAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    private AccountDetail destinationAccount;



}
