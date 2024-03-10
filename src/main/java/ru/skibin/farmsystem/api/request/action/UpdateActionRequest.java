package ru.skibin.farmsystem.api.request.action;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class UpdateActionRequest {
    @NotNull(message = "Profile id can't be null")
    @Positive(message = "Profile id must be positive")
    private Long profileId;
    @NotNull(message = "Product id can't be null")
    @Positive(message = "Product idd must be positive")
    private Long productId;
    @NotNull(message = "Message can't be null")
    private Float value;
    private Instant time;
    private Boolean isActual;
}
