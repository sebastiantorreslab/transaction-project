package com.api.ms_transaction.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CurrencyAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigDecimal id;
    private String code;
    private String country;

    @OneToOne
    private AccountDetail accountDetail;





}
