package com.ye.bank;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info (
                title = "Spring Bank Backend Demo" ,
                description = "Backend REST API for Practice" ,
                version = "v1.0.1",
                contact = @Contact (
                        name = "Ye Wint Hlaing",
                        email = "yewinthlaing04@gmail.com",
                        url = "https://github.com/yewinthlaing04/bank-backend"
                )
        )
)
public class BankBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankBackEndApplication.class, args);
    }

}
