package com.ye.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionSaveDto {

    private String transactionType;
    private BigDecimal amount;
    private String accountNumber;
    private String status;
}
