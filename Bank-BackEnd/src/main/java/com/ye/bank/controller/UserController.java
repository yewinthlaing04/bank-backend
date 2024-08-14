package com.ye.bank.controller;

import com.ye.bank.dto.BankResponse;
import com.ye.bank.dto.UserRequest;
import com.ye.bank.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public BankResponse createUser(@RequestBody UserRequest userRequest){

        return userService.createAccount(userRequest);
    }
}
