package com.ye.bank.service;

import com.ye.bank.dto.BankResponse;
import com.ye.bank.dto.UserRequest;

public interface IUserService {

    // to create account
    BankResponse createAccount(UserRequest userRequest);
}
