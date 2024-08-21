package com.ye.bank.service.impl;

import com.ye.bank.entity.Transaction;
import com.ye.bank.repository.TransactionRepo;
import com.ye.bank.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService implements ITransactionService {

    @Autowired
    private TransactionRepo transactionRepo;

    @Override
    public void saveTransaction(Transaction transaction) {

    }

}
