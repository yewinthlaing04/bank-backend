package com.ye.bank.utils;

import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE= "This user already exists with this email";

    public static final String ACCOUNT_CREATION_CODE = "002";
    public static final String ACCOUNT_CREATION_MESSAGE = "Account has been successfully created";

    public static final String ACCOUNT_NOT_EXIST_CODE = "003";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE = "This user does not exist with this account number";

    public static final String ACCOUNT_FOUND_CODE = "004";
    public static final String ACCOUNT_FOUND_MESSAGE = "User Account Found";

    public static final String ACCOUNT_CREDITED_CODE = "005";
    public static final String ACCOUNT_CREDITED_MESSAGE = "Credited account has been successfully created";

    public static final String INSUFFICIENT_BALANCE_CODE = "006";
    public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient balance";

    public static final String ACCOUNT_DEBITED_SUCCESS_CODE = "007";
    public static final String ACCOUNT_DEBITED_SUCCESS_MESSAGE = "Debited account has been successfully created";

    public static final String TRANSACTION_SUCCESSFUL_CODE = "008";
    public static final String TRANSACTION_SUCCESSFUL_MESSAGE = "Transaction has been successfully created";

    // random 6 digit + year for creating bank account
    public static String generateAccountNumber(){

        Year currentYear = Year.now();

        int min = 100000;

        int max = 999999;

        // generate random number between min and max

        int randomNumber = (int) Math.floor( Math.random() * ( max - min + 1 ) + min ) ;

        // convert to string
        String year = String.valueOf(currentYear);
        String number = String.valueOf(randomNumber);

        StringBuilder accountNumber = new StringBuilder();

        return  accountNumber.append(year).append(number).toString();
    }


}
