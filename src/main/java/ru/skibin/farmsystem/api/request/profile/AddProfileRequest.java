package ru.skibin.farmsystem.api.request.profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddProfileRequest {
    @NotNull
    @NotEmpty
    @Size(min = 2, max = 50, message = "FIO size: 2-50")
    private String fio;
    @NotNull
    @NotEmpty
    @Email(message = "wrong email")
    private String email;
    @NotNull
    @NotEmpty
    @Size(min = 5, message = "min password size - 5")
    private String password;
    private Boolean isAdmin;
}