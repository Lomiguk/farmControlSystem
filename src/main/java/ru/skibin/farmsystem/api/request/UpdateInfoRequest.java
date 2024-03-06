package ru.skibin.farmsystem.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateInfoRequest {
    @NotNull
    @Size(min = 2, max = 50)
    String fio;
    @NotNull
    @Email
    String email;
}
