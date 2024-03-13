package ru.skibin.farmsystem.api.request.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skibin.farmsystem.api.enumTypes.Role;

@Data
@AllArgsConstructor
@Schema(description = "Request with new date for updating profile")
public class UpdateProfileRequest {
    @NotNull
    @Size(min = 5, message = "min password size - 5")
    @Schema(description = "Old password")
    private String oldPassword;
    @NotNull
    @Size(min = 2, max = 50, message = "profile name size 2-50 chars")
    @Schema(description = "new fio: f - Surname, i - name, o - patronymic")
    private String fio;
    @NotNull
    @Size(min = 5, message = "min password size - 5")
    @Schema(description = "New password")
    private String newPassword;
    @Schema(description = "new role - set of permissions")
    private Role role;
    @Schema(description = "New actuality status for profile")
    private Boolean isActual;
}
