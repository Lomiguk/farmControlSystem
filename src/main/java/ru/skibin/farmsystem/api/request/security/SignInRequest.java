package ru.skibin.farmsystem.api.request.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignInRequest {
    @NotNull
    @NotBlank(message = "Email can't be blank")
    @Email(message = "Wrong email format")
    private String email;
    @NotNull
    @Size(min = 5, message = "min password size - 5")
    private String password;
}
