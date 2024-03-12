package ru.skibin.farmsystem.api.request.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skibin.farmsystem.api.enumTypes.Role;

@Data
@AllArgsConstructor
public class SignUpRequest {
    @NotNull
    @Size(min = 2, max = 50, message = "\"profile name size 2-50 chars\"")
    private String fio;
    @NotNull
    @NotBlank(message = "Email can't be blank")
    @Email(message = "Wrong email format")
    private String email;
    @NotNull
    @Size(min = 5, message = "min password size - 5")
    private String password;
    private Role role;
}
