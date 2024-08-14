package com.ye.bank.service;

import com.ye.bank.dto.EmailDetails;

public interface IEmailService {

    void sendEmail(EmailDetails emailDetails);
}
