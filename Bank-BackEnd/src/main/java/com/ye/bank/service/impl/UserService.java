package com.ye.bank.service.impl;

import com.ye.bank.dto.AccountInfo;
import com.ye.bank.dto.BankResponse;
import com.ye.bank.dto.EmailDetails;
import com.ye.bank.dto.UserRequest;
import com.ye.bank.entity.UserEntity;
import com.ye.bank.repository.UserRepo;
import com.ye.bank.service.IUserService;
import com.ye.bank.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailService emailService;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {

        // check email exists
        boolean isExistEmail = userRepo.existsByEmail(userRequest.getEmail());

        // if email exists , return bank response with error code and message
        if ( isExistEmail ){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .info(null)
                    .build();
        }

        // creating an account
        UserEntity newUser = UserEntity.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .email(userRequest.getEmail())
                .address(userRequest.getAddress())
                .stateOfBirth(userRequest.getStateOfBirth())
                .phoneNumber(userRequest.getPhoneNumber())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .build();

        UserEntity savedUser = userRepo.save(newUser);

        // send email alert
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("Bank Account Creation")
                .messageBody("Congratulation! Your Acocunt has been successfully created!\n Your Account Details \n "
                        + savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getEmail() + " " + savedUser.getAccountNumber())
                .build();

        emailService.sendEmail(emailDetails);


        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .info(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + "" + savedUser.getLastName())
                        .build())
                .build();
    }
}
