package com.api.ms_transaction.registry.request;

import lombok.Builder;

import java.math.BigDecimal;
@Builder
public record transactionRequestDTO(String currencyOrigin,
                                    String currencyDestination,
                                    String sourceAccountRef,
                                    String destinationAccountRef,
                                    BigDecimal amount,
                                    String transactionType) {
}
