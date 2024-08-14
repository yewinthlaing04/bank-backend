package com.ye.bank.utils;

import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE= "This user already exists with this email";

    public static final String ACCOUNT_CREATION_CODE = "002";
    public static final String ACCOUNT_CREATION_MESSAGE = "Account has been successfully created";

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
