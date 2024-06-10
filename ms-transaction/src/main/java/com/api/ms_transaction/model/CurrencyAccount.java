package com.api.ms_transaction.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

import static com.api.ms_transaction.util.Converter.*;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountDetail_id")
    private AccountDetail accountDetail;

    public static CurrencyAccount setCurrencyAccount(String country){

        CurrencyAccount currencyAccount = new CurrencyAccount();

        if(Objects.equals(country, "colombia")){
            currencyAccount.code = COP;
            currencyAccount.country = "colombia";
        }
        if(Objects.equals(country, "argentina")){
            currencyAccount.code = ARS;
            currencyAccount.country = "argentina";
        }
        if(Objects.equals(country, "usa")){
            currencyAccount.code = USD;
            currencyAccount.country = "united states";

        }
        else if (!country.isEmpty() && !country.equalsIgnoreCase("colombia") && !country.equalsIgnoreCase("argentina") && !country.equalsIgnoreCase("usa")){
            currencyAccount.code = EUR;
            currencyAccount.country = country;
        }

        return currencyAccount;
    }








}
