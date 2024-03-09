package ru.skibin.farmsystem.api.request.action;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class UpdateActionRequest {
    @NotNull(message = "")
    @Positive(message = "")
    private Long profileId;
    @NotNull
    private Long productId;
    @NotNull
    private Float value;
    @NotNull
    private Instant time;

    private Boolean isActual;
}
