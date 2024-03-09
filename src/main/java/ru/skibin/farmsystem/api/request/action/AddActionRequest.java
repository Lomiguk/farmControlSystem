package ru.skibin.farmsystem.api.request.action;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class AddActionRequest {
    @NotNull
    @Positive
    Long profileId;
    @NotNull
    @Positive
    Long productId;
    @NotNull
    @Positive
    Float value;
    @NotNull
    Instant time;
}
