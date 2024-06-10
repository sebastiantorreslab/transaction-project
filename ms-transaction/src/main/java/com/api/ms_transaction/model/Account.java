package com.api.ms_transaction.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @UuidGenerator
    private String account_ref;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private AccountDetail accountDetail;

    public void setAccountDetail(AccountDetail accountDetail) {
        this.accountDetail = accountDetail;
        if (accountDetail != null) {
            accountDetail.setAccount(this);
        }
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", account_ref='" + account_ref + '\'' +
                ", accountDetail=" + accountDetail +
                '}';
    }


}
