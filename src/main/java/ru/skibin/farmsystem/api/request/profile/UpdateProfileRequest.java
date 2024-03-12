package ru.skibin.farmsystem.api.request.profile;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skibin.farmsystem.api.enumTypes.Role;

@Data
@AllArgsConstructor
public class UpdateProfileRequest {
    @NotNull
    @Size(min = 5, message = "min password size - 5")
    private String oldPassword;
    @NotNull
    @Size(min = 2, max = 50, message = "\"profile name size 2-50 chars\"")
    private String fio;
    @NotNull
    @Size(min = 5, message = "min password size - 5")
    private String newPassword;
    private Role role;
    private Boolean isActual;
}
