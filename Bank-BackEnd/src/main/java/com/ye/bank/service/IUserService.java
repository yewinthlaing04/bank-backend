package com.ye.bank.service;

import com.ye.bank.dto.*;


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
    BankResponse transaction( TransactionDto transactionDto);
}
