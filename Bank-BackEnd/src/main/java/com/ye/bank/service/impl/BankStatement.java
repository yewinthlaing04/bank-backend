package com.ye.bank.service.impl;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ye.bank.dto.EmailDetails;
import com.ye.bank.entity.Transaction;
import com.ye.bank.entity.UserEntity;
import com.ye.bank.repository.TransactionRepo;
import com.ye.bank.repository.UserRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatement {

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailService emailService;

    private static final String FILE = "C:\\Users\\User\\BankStatement\\MyStatement1.pdf";
    /**
     * retrieve list of transaction within a date
     * generate a pdf file of transaction list
     * send the file via email
     */

    // list all transaction with account number
    public List<Transaction> generateStatement(String accountNumber ,
                                               String startDate, String endDate) throws FileNotFoundException, DocumentException {

        // convert string date to localdate
        LocalDate start = LocalDate.parse( startDate , DateTimeFormatter.ISO_DATE);

        LocalDate end = LocalDate.parse( endDate , DateTimeFormatter.ISO_DATE);

        // retrive from db for user
        UserEntity user = userRepo.findByAccountNumber(accountNumber);

        String customerName = user.getFirstName() + user.getLastName();
        //String customerAccount = user.getAccountNumber();

        List<Transaction> transactionList =  transactionRepo.findAll()
                .stream()
                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter( transaction -> transaction.getCreatedAt().isEqual( start ))
                .filter( transaction ->  transaction.getCreatedAt().isEqual(end))
                .toList();

        // formatting pdf
        Rectangle statementSize = new Rectangle(PageSize.A4);

        Document document = new Document(statementSize);

        log.info( "setting size of document");

        OutputStream outputStream = new FileOutputStream(FILE);

        PdfWriter.getInstance(document , outputStream );

        document.open();

        PdfPTable bankInfoTable = new PdfPTable(1);
        PdfPCell bankName = new PdfPCell( new Phrase( "Neo Bank"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.BLUE);
        bankName.setPadding(20f);

        PdfPCell bankAddress = new PdfPCell(new Phrase( "34, Some Address , Myanmar"));
        bankAddress.setBorder(0);
        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);

        PdfPTable statementInfo = new PdfPTable(2);
        PdfPCell customerInfo = new PdfPCell( new Phrase( "Start Date: " + start ));
        customerInfo.setBorder(0);

        PdfPCell statement = new PdfPCell( new Phrase( "STATEMENT OF ACCOUNT"));
        statement.setBorder(0);

        PdfPCell end_date = new PdfPCell( new Phrase ( "End Date: " + end));
        end_date.setBorder(0);

        PdfPCell name = new PdfPCell( new Phrase( "Customer Name: " + user.getFirstName() + " " + user.getLastName()));
        name.setBorder(0);

        PdfPCell space = new PdfPCell( );
        space.setBorder(0);

        PdfPCell address = new PdfPCell(new Phrase( "Customer Address: " +  user.getAddress()));
        address.setBorder(0);

        // transaction table

        PdfPTable transactionTable = new PdfPTable(4);

        PdfPCell date = new PdfPCell( new Phrase( "DATE"));
        date.setBackgroundColor(BaseColor.BLUE);
        date.setBorder(0);

        PdfPCell transactionType = new PdfPCell(new Phrase( "TRANSACTION TYPE"));
        transactionType.setBackgroundColor(BaseColor.BLUE);
        transactionType.setBorder(0);

        PdfPCell transactionAmount = new PdfPCell(new Phrase( "TRANSACTION AMOUNT"));
        transactionAmount.setBackgroundColor(BaseColor.BLUE);
        transactionAmount.setBorder(0);

        PdfPCell transactionStatus = new PdfPCell(new Phrase( "TRANSACTION STATUS"));
        transactionStatus.setBackgroundColor(BaseColor.BLUE);
        transactionStatus.setBorder(0);

        transactionTable.addCell(date);
        transactionTable.addCell(transactionType);
        transactionTable.addCell(transactionAmount);
        transactionTable.addCell(transactionStatus);

        transactionList.forEach( transaction -> {
            transactionTable.addCell( new Phrase( transaction.getCreatedAt().toString() ));
            transactionTable.addCell( new Phrase( transaction.getTransactionType()));
            transactionTable.addCell( new Phrase( transaction.getAmount().toString()));
            transactionTable.addCell( new Phrase( transaction.getStatus()));
        });

        statementInfo.addCell( customerInfo);
        statementInfo.addCell( statement);
        statementInfo.addCell(end_date);
        statementInfo.addCell(name);
        statementInfo.addCell(space);
        statementInfo.addCell(address);

        document.add(bankInfoTable);
        document.add(statementInfo);
        document.add(transactionTable);

        document.close();

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("STATEMENT OF ACCOUNT")
                .messageBody("Kindly Find your requested Bank Statement !")
                .attachment(FILE)
                .build();

        emailService.sendEmailWithAttachment(emailDetails);

        return transactionList;
    }


}
