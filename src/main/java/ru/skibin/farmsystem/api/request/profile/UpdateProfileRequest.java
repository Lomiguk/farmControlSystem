package ru.skibin.farmsystem.api.request.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skibin.farmsystem.api.data.enumTypes.Role;

@Data
@AllArgsConstructor
@Schema(description = "Request with new date for updating profile")
public class UpdateProfileRequest {
    @NotBlank(message = "Password can't be blank")
    @Size(min = 5, message = "Min password size - 5")
    @Schema(description = "Old password")
    private String oldPassword;
    @NotBlank(message = "Fio can't be blank")
    @Size(min = 2, max = 50, message = "Profile name size 2-50 chars")
    @Schema(description = "New fio: f - Surname, i - name, o - patronymic")
    private String fio;
    @NotBlank(message = "Password can't be blank")
    @Size(min = 5, message = "Min password size - 5")
    @Schema(description = "New password")
    private String newPassword;
    @Schema(description = "New role - set of permissions")
    private Role role;
    @Schema(description = "New actuality status for profile")
    private Boolean isActual;
}
