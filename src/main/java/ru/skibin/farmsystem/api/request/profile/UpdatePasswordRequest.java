package ru.skibin.farmsystem.api.request.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Request with new & old passwords")
public class UpdatePasswordRequest {
    @NotNull
    @Size(min = 5, message = "min password size - 5")
    @Schema(description = "old password")
    private String oldPassword;
    @NotNull
    @Size(min = 5, message = "min password size - 5")
    @Schema(description = "new password")
    private String newPassword;
}
