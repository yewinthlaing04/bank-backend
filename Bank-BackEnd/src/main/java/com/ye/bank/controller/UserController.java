package com.ye.bank.controller;

import com.ye.bank.dto.*;
import com.ye.bank.service.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag( name = " User Bank Account RESP API")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(
            summary = "Create New Bank Account",
            description = "Creating a new user and assigning an account ID"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status 201 SUCCESS"
    )
    @PostMapping("/create")
    public BankResponse createUser(@RequestBody UserRequest userRequest){
        return userService.createAccount(userRequest);
    }


    @Operation(
            summary = "Balance Enquiry",
            description = "Checking the balance"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status 201 SUCCESS"
    )
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

    @PostMapping("/transfer")
    public BankResponse transferUser(@RequestBody TransactionDto transactionDto) {
        return userService.transaction(transactionDto);
    }
}
