package com.ye.bank.service;

import com.ye.bank.dto.BankResponse;
import com.ye.bank.dto.CreditDto;
import com.ye.bank.dto.EnquiryRequest;
import com.ye.bank.dto.UserRequest;



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

}
