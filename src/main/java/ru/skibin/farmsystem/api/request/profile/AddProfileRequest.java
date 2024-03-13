package ru.skibin.farmsystem.api.request.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skibin.farmsystem.api.data.enumTypes.Role;

@Data
@AllArgsConstructor
@Schema(description = "Request with new profile data")
public class AddProfileRequest {
    @NotNull
    @Size(min = 2, max = 50, message = "profile name size 2-50 chars")
    @Schema(description = "User's fio: f - Surname, i - name, o - patronymic")
    private String fio;
    @NotNull
    @NotBlank(message = "Email can't be blank")
    @Email(message = "Wrong email format")
    @Schema(description = "Profile email/login")
    private String email;
    @NotNull
    @Size(min = 5, message = "min password size - 5")
    @Schema(description = "Profile password")
    private String password;
    @Schema(description = "Profile role - set of permissions")
    private Role role;
}
