package ru.skibin.farmsystem.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddProfileRequest {
    @NotNull
    @Size(min = 2, max = 50, message = "FIO size: 2-50")
    private String name;
    @NotNull
    @Email(message = "wrong email")
    private String email;
    @NotNull
    @Size(min = 5, message = "min password size - 5")
    private String password;
    private Boolean isAdmin;
}
