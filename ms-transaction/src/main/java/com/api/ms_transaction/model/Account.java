package com.api.ms_transaction.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigDecimal id;

    @NaturalId
    @UuidGenerator
    private String accountId;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToOne
    private AccountDetail accountDetail;


}
