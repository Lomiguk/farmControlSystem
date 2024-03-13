package ru.skibin.farmsystem.api.request.action;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Schema(description = "Request for adding new action")
@Data
@AllArgsConstructor
public class AddActionRequest {
    @NotNull
    @Positive
    @Schema(description = "Responsible profile numerical identifier")
    Long profileId;
    @NotNull
    @Positive
    @Schema(description = "Product numerical identifier")
    Long productId;
    @NotNull
    @Positive
    @Schema(description = "Product value")
    Float value;
    @NotNull
    @Schema(description = "Time of action")
    Instant time;
}
