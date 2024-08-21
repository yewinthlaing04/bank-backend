package com.ye.bank.service.impl;

import com.ye.bank.dto.*;
import com.ye.bank.entity.UserEntity;
import com.ye.bank.repository.UserRepo;
import com.ye.bank.service.IUserService;
import com.ye.bank.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TransactionService transactionService;

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

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {

        // check account number exist
        boolean isAccountExist = userRepo.existsByAccountNumber(enquiryRequest.getAccountNumber());

        if (!isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .info(null)
                    .build();
        }

        // if exist user , find with account number
        UserEntity existUser = userRepo.findByAccountNumber(enquiryRequest.getAccountNumber());

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                .info( AccountInfo.builder()
                        .accountBalance(existUser.getAccountBalance())
                        .accountNumber(enquiryRequest.getAccountNumber())
                        .accountName(existUser.getFirstName() + " " + existUser.getLastName())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {

        // ckeck account number exist
        boolean isAccountExist = userRepo.existsByAccountNumber(enquiryRequest.getAccountNumber());

        if ( !isAccountExist){
            return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
        }

        UserEntity existUser = userRepo.findByAccountNumber(enquiryRequest.getAccountNumber());

        return existUser.getFirstName() + " " + existUser.getLastName() ;
    }

    @Override
    public BankResponse creditAccount(CreditDto creditDto) {

        // check if account exist
        boolean isAccountExist = userRepo.existsByAccountNumber(creditDto.getAccountNumber());
        if ( !isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .info(null)
                    .build();
        }
        // find with account number
        UserEntity userToCredit = userRepo.findByAccountNumber(creditDto.getAccountNumber());

        // set balance amount
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(creditDto.getAmount()));

        // save user
        userRepo.save(userToCredit);

        // transaction save
        TransactionSaveDto transactionSaveDto = TransactionSaveDto.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactionType("CREDIT")
                .amount(creditDto.getAmount())
                .build();

        transactionService.saveTransaction(transactionSaveDto);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_MESSAGE)
                .info( AccountInfo.builder()
                        .accountName( userToCredit.getFirstName() + " " + userToCredit.getLastName())
                        .accountBalance( userToCredit.getAccountBalance())
                        .accountNumber( creditDto.getAccountNumber())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDto creditDto) {

        // check if account exist
        boolean isAccountExist = userRepo.existsByAccountNumber(creditDto.getAccountNumber());
        if ( !isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .info(null)
                    .build();
        }

        // check if amount you intend to withdraw isn't more than current balance

        UserEntity userToDeposit = userRepo.findByAccountNumber(creditDto.getAccountNumber());

        // convert bigdecimal to integer
        BigInteger availableBalance = userToDeposit.getAccountBalance().toBigInteger();
        BigInteger debitAmount = creditDto.getAmount().toBigInteger();

        if ( availableBalance.intValue() < debitAmount.intValue() ){
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .info(null)
                    .build();
        }else{
            userToDeposit.setAccountBalance(
                    userToDeposit.getAccountBalance()
                            .subtract(creditDto.getAmount()));
            userRepo.save(userToDeposit);

            // transaction save
            TransactionSaveDto transactionSaveDto = TransactionSaveDto.builder()
                    .accountNumber(userToDeposit.getAccountNumber())
                    .transactionType("DEBIT")
                    .amount(creditDto.getAmount())
                    .build();

            transactionService.saveTransaction(transactionSaveDto);


            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
                    .info(AccountInfo.builder()
                            .accountName( userToDeposit.getFirstName() + " " + userToDeposit.getLastName())
                            .accountBalance( userToDeposit.getAccountBalance())
                            .accountNumber( creditDto.getAccountNumber())
                            .build())
                    .build();
        }

    }

    @Override
    public BankResponse transaction(TransactionDto transactionDto) {

        // get the account to debit
        // check if the amount ( transfer ) is not more than current balance
        // debit the account
        // get the account to credit
        // check the destination account and credit the account
        // save

        boolean isDestinationAccountExist = userRepo
                .existsByAccountNumber(transactionDto.getDestinationAccountNumber());

        if ( !isDestinationAccountExist ){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .info(null)
                    .build();
        }

        UserEntity sourceAccount = userRepo
                .findByAccountNumber(transactionDto.getSourceAccountNumber());

        if ( transactionDto.getAmount().compareTo(sourceAccount.getAccountBalance()) > 0 ){
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .info(null)
                    .build();
        }

        sourceAccount.setAccountBalance(sourceAccount.getAccountBalance().subtract(transactionDto.getAmount()));

        String sourceUsername = sourceAccount.getFirstName() + sourceAccount.getLastName();

        userRepo.save(sourceAccount);


        EmailDetails debitAlert = EmailDetails.builder()
                .subject("DEBIT ALERT")
                .recipient(sourceAccount.getEmail())
                .messageBody("The sum of " + transactionDto.getAmount() +
                        " has been deducted from your account to ! Your current balance is " + sourceAccount.getAccountBalance())
                .build();
        emailService.sendEmail(debitAlert);

        UserEntity destinationAccount = userRepo.findByAccountNumber(transactionDto
                .getDestinationAccountNumber());

        destinationAccount.setAccountBalance( destinationAccount
                .getAccountBalance().add(transactionDto.getAmount()));
        userRepo.save(destinationAccount);



        //String recipientName = destinationAccount.getFirstName() + destinationAccount.getLastName();
        EmailDetails creditAlert = EmailDetails.builder()
                .subject("CREDIT ALERT")
                .recipient(destinationAccount.getEmail())
                .messageBody("The sum of " + transactionDto.getAmount() +
                        " has been sent to your account from " + sourceUsername)
                .build();
        emailService.sendEmail(creditAlert);

        //save transaction
        TransactionSaveDto transactionSaveDto = TransactionSaveDto.builder()
                .accountNumber(destinationAccount.getAccountNumber())
                .transactionType("CREDIT")
                .amount(transactionDto.getAmount())
                .build();

        transactionService.saveTransaction(transactionSaveDto);

        return BankResponse.builder()
                .responseCode(AccountUtils.TRANSACTION_SUCCESSFUL_CODE)
                .responseMessage(AccountUtils.TRANSACTION_SUCCESSFUL_MESSAGE)
                .info(null)
                .build();
    }
}
