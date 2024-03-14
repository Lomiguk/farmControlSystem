package ru.skibin.farmsystem.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skibin.farmsystem.api.data.enumTypes.Role;

@Data
@Schema(description = "Profile response")
public class ProfileResponse {
    @Schema(description = "Profile's numerical identifier")
    private final Long id;
    @Schema(description = "User's fio: f - Surname, i - name, o - patronymic")
    private final String fio;
    @Schema(description = "Profile's email/login")
    private final String email;
    @Schema(description = "Profile's role - set of permissions")
    private final Role role;
    @Schema(description = "Profile's actuality status")
    private final Boolean isActual;
}
