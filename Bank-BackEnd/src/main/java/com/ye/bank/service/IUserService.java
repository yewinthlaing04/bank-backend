package com.ye.bank.service;

import com.ye.bank.dto.*;
import org.springframework.transaction.annotation.Transactional;


public interface IUserService {

    // to create account
    BankResponse createAccount(UserRequest userRequest);

    // balance Enquiry
    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);

    // name Enquiry
    String nameEnquiry( EnquiryRequest enquiryRequest);

    // credit Account
    BankResponse creditAccount(CreditDto creditDto);

    // withdraw money
    BankResponse debitAccount(CreditDto creditDto);

    // transaction
    @Transactional
    BankResponse transaction( TransactionDto transactionDto);
}
