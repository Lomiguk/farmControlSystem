package ru.skibin.farmsystem.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.skibin.farmsystem.api.data.enumTypes.Role;

@Data
@RequiredArgsConstructor
@Schema(description = "Worker data")
public class WorkerResponse {
    @Schema(description = "Worker's profile id")
    private final Long profileId;
    @Schema(description = "Worker's profile fio")
    private final String profileFio;
    @Schema(description = "Worker's profile email")
    private final String profileEmail;
    @Schema(description = "Worker's role")
    private final Role profileRole;
}
