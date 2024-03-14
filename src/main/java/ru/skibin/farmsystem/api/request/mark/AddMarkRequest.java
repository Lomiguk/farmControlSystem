package ru.skibin.farmsystem.api.request.mark;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Request with data for adding new mark")
public class AddMarkRequest {
    @NotNull(message = "Profile id cant be null")
    @Positive(message = "Profile id must be positive")
    @Schema(description = "Profile id")
    private final Long profileId;
    @Positive(message = "Mark must be positive")
    @Max(value = 100, message = "mark value: 0-100")
    @Schema(description = "mark")
    private final Integer mark;
    @Schema(description = "date")
    private final LocalDate date;
}
