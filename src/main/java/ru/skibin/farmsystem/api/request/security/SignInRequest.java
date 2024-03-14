package ru.skibin.farmsystem.api.request.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Sign-in request")
public class SignInRequest {
    @NotBlank(message = "Email can't be blank")
    @Email(message = "Wrong email format")
    private String email;
    @NotBlank(message = "Password can't be blank")
    @Size(min = 5, message = "min password size - 5")
    private String password;
}
