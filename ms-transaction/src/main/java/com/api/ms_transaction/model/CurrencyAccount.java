package com.api.ms_transaction.model;

import com.api.ms_transaction.repository.ICurrencyAccountRepository;
import com.api.ms_transaction.service.ICurrencyAccountService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

import static com.api.ms_transaction.util.Converter.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CurrencyAccount {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String country;

    @OneToMany(mappedBy = "accountCurrency")
    @JsonIgnore
    private Set<AccountDetail> accountDetailSet = new HashSet<>();


    public static CurrencyAccount setCurrencyAccount(String country, ICurrencyAccountService currencyAccountService) {
        CurrencyAccount currencyAccount;

        if (currencyAccountService.existsCurrencyAccountByCountry(country)) {
            currencyAccount = currencyAccountService.findCurrencyAccountByCountry(country);
        } else {
            currencyAccount = new CurrencyAccount();
            if (country.equalsIgnoreCase("colombia")) {
                currencyAccount.code = COP;
                currencyAccount.country = "colombia";
            }
            if (country.equalsIgnoreCase("argentina")) {
                currencyAccount.code = ARS;
                currencyAccount.country = "argentina";
            }
            if (country.equalsIgnoreCase("usa")) {
                currencyAccount.code = USD;
                currencyAccount.country = "united states";

            } else if (!country.isEmpty() && !country.equalsIgnoreCase("colombia") && !country.equalsIgnoreCase("argentina") && !country.equalsIgnoreCase("usa")) {
                currencyAccount.code = EUR;
                currencyAccount.country = country;
            }

        }
        currencyAccountService.create(currencyAccount);
        return currencyAccount;
    }

    public void addAccountToCurrencyAccount(AccountDetail accountDetail) {
        accountDetailSet.add(accountDetail);
        accountDetail.setAccountCurrency(this);
    }



}
