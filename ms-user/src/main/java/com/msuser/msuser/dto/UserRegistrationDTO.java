package com.msuser.msuser.dto;

import com.msuser.msuser.util.ValidPassword;
import jakarta.validation.constraints.*;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import java.util.Set;


@Builder
public record UserRegistrationDTO(

        @NotBlank(message = "Username is mandatory")
        @Length(min = 3,max = 15,message = "username must have between 3 and 15 characters")
        String username,

        @NotBlank(message = "Blank not allowed in email")
        @Pattern(regexp = "^(.+)@(.+)$", message = "email field example@domain.com")
        String email,

        @NotBlank(message = "first name is mandatory")
        @Length(min = 3,max = 25,message = "first name must have between 3 and 25 characters")
        String firstName,

        @NotBlank(message = "last name is mandatory")
        @Length(min = 3,max = 25,message = "last name must have between 3 and 25 characters")
        String lastName,

        @NotNull
        @NotBlank
        @ValidPassword
        String pass,

        String country,

        @Size(min = 1, max = 5)
        Set<String> roles) {


}
