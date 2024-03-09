package ru.skibin.farmsystem.api.request.profile;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdatePasswordRequest {
    @NotNull
    @Size(min = 5, message = "min password size - 5")
    private String oldPassword;
    @NotNull
    @Size(min = 5, message = "min password size - 5")
    private String newPassword;
}
