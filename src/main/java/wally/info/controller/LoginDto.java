package wally.info.controller;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class LoginDto {
    @NotBlank(message = "Username must not be null or empty")
    @Size(max = 300, message = "Maximum size for the username is 200 characters")
    private String username;

    @NotBlank(message = "Password must not be null or empty")
    @Size(max = 250, message = "Maximum size for the password is 250 characters")
    private String password;
}
