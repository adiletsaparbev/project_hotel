package com.example.it_proger.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Getter
@Setter
public class RegisterDto {

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;
    @NotNull(message = "Дата рождения обязательна")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @NotEmpty
    @Email
    private String email;

    private String phone;

    private String address;

    @Size(min = 6, message = "Minimum Password length 6 characters")
    private String password;
    private String confirmPassword;

}
