package ru.skibin.farmsystem.entity;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@RequiredArgsConstructor
public class TaskEntity {
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
