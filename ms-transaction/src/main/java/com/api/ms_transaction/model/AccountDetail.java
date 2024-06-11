package com.api.ms_transaction.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import javax.print.attribute.standard.DateTimeAtCreation;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "cuenta_id", nullable = false)
    @JsonIgnore
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "currency_account_id")
    private CurrencyAccount accountCurrency;

    private BigDecimal balance;
    private Date createAt;
    private Boolean isEnabled;
    private String createdBy;
    private Date lastUpdateAt;

    @OneToMany(mappedBy = "sourceAccount", cascade = {CascadeType.MERGE,CascadeType.PERSIST}, orphanRemoval = true)
    private Set<Transaction> incomingTransactions = new HashSet<>();

    @OneToMany(mappedBy = "destinationAccount", cascade = {CascadeType.MERGE,CascadeType.PERSIST}, orphanRemoval = true)
    private Set<Transaction> outgoingTransactions = new HashSet<>();


    @Override
    public String toString() {
        return "AccountDetail{" +
                "id=" + id +
                ", account=" + account +
                ", balance=" + balance +
                ", createAt=" + createAt +
                ", isEnabled=" + isEnabled +
                ", createdBy='" + createdBy + '\'' +
                ", lastUpdateAt=" + lastUpdateAt +
                '}';
    }

    public void add(Transaction transaction, Account source, Account destination) {
        incomingTransactions.add(transaction);
        outgoingTransactions.add(transaction);
        transaction.setSourceAccount(source.getAccountDetail());
        transaction.setDestinationAccount(destination.getAccountDetail());
    }
}
