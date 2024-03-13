package ru.skibin.farmsystem.api.request.action;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
@Schema(description = "Request for updating action")
public class UpdateActionRequest {
    @NotNull(message = "Profile id can't be null")
    @Positive(message = "Profile id must be positive")
    @Schema(description = "New responsible profile numerical identifier")
    private Long profileId;
    @NotNull(message = "Product id can't be null")
    @Positive(message = "Product idd must be positive")
    @Schema(description = "New Product numerical identifier")
    private Long productId;
    @NotNull(message = "Message can't be null")
    @Schema(description = "New product value")
    private Float value;
    @Schema(description = "Time of action")
    private Instant time;
    @Schema(description = "Status of the actuality")
    private Boolean isActual;
}
