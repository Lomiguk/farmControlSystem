package ru.skibin.farmsystem.api.request.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpRequest {
    @NotBlank(message = "Fio can't be blank")
    @Size(min = 2, max = 50, message = "Profile name size 2-50 chars")
    private String fio;
    @NotNull
    @NotBlank(message = "Email can't be blank")
    @Email(message = "Wrong email format")
    private String email;
    @NotNull
    @Size(min = 5, message = "Min password size - 5")
    private String password;
}
