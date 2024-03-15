package ru.skibin.farmsystem.api.request.task;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class UpdateTaskRequest {
    @NotNull(message = "Start date can't be null")
    private final LocalDate startDate;
    @NotNull(message = "End date can't be null")
    private final LocalDate endDate;
    @NotNull(message = "Description can't be null")
    private final String description;
    @NotNull(message = "Profile id can't be null")
    @Positive(message = "Product id must be positive")
    private final Long profileId;
    @NotNull(message = "Product id can't be null")
    @Positive(message = "Product id must be positive")
    private final Long productId;
    @NotNull(message = "Product value can't be null")
    @Positive(message = "Product value mast be positive")
    private final Float value;
    @NotNull
    private final Float collectedValue;
    @NotNull
    private final Boolean isDone;
    @NotNull
    private final Boolean isAborted;
}
