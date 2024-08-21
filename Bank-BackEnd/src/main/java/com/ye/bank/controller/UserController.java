package com.ye.bank.controller;

import com.ye.bank.dto.BankResponse;
import com.ye.bank.dto.CreditDto;
import com.ye.bank.dto.EnquiryRequest;
import com.ye.bank.dto.UserRequest;
import com.ye.bank.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public BankResponse createUser(@RequestBody UserRequest userRequest){

        return userService.createAccount(userRequest);
    }

    @GetMapping("/balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return userService.balanceEnquiry(enquiryRequest);
    }

    @GetMapping("/nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return userService.nameEnquiry(enquiryRequest);
    }

    @PostMapping("/credit")
    public BankResponse creditUser (@RequestBody CreditDto creditDto ) {
        return userService.creditAccount( creditDto);
    }

    @PostMapping("/debit")
    public BankResponse debitUser( @RequestBody CreditDto creditDto ){
        return userService.debitAccount( creditDto);
    }
}
