package ru.skibin.farmsystem.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class MarkEntity {
    private final Long id;
    private final Long profileId;
    private final Integer mark;
    private final LocalDate date;
}
