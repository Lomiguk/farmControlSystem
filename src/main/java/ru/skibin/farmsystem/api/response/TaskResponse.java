package ru.skibin.farmsystem.api.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class TaskResponse {
    private final Long id;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String description;
    private final Long profileId;
    private final Long productId;
    private final Float value;
    private final Float collectedValue;
    private final Boolean isDone;
    private final Boolean isAborted;
}
