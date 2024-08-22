package com.ye.bank.controller;

import com.itextpdf.text.DocumentException;
import com.ye.bank.entity.Transaction;
import com.ye.bank.service.impl.BankStatement;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/bankstatement")
@AllArgsConstructor
public class TransactionController {

    private BankStatement bankStatement;

    @GetMapping
    public List<Transaction> generateTransactions(@RequestParam String accountNumber ,
                                                  @RequestParam String startDate ,
                                                  @RequestParam String endDate) throws DocumentException, FileNotFoundException {

        return bankStatement.generateStatement( accountNumber, startDate, endDate);

    }



}
