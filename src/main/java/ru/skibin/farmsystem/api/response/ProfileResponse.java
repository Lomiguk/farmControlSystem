package ru.skibin.farmsystem.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skibin.farmsystem.api.enumTypes.Role;

@Data
@AllArgsConstructor
@Schema(description = "Profile response")
public class ProfileResponse {
    @Schema(description = "Profile's numerical identifier")
    private Long id;
    @Schema(description = "User's fio: f - Surname, i - name, o - patronymic")
    private String fio;
    @Schema(description = "Profile's email/login")
    private String email;
    @Schema(description = "Profile's role - set of permissions")
    private Role role;
    @Schema(description = "Profile's actuality status")
    private Boolean isActual;
}
