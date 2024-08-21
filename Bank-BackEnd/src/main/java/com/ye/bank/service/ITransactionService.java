package com.ye.bank.service;

import com.ye.bank.dto.TransactionSaveDto;
import com.ye.bank.entity.Transaction;

public interface ITransactionService {

    // save transaction
    void saveTransaction(TransactionSaveDto transactionSaveDto);

}
