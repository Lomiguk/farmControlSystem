package ru.skibin.farmsystem.api.request.mark;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class UpdateMarkRequest {
    @NotNull(message = "Profile id cant be null")
    @Positive(message = "Profile id must be positive")
    @Schema(description = "New profile id")
    private final Long profileId;
    @Positive(message = "Mark must be positive")
    @Max(value = 100, message = "mark value: 0-100")
    @Schema(description = "New mark")
    private final Integer mark;
    @Schema(description = "new date")
    @NotNull(message = "Mark Date can't be null")
    private final LocalDate date;
}
