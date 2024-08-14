package com.ye.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {


    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String stateOfBirth;
    private String phoneNumber;
    private String alternativePhoneNumber;

}
