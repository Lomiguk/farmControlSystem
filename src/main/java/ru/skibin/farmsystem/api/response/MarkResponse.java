package ru.skibin.farmsystem.api.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MarkResponse {
    private final Long id;
    private final Long profileId;
    private final Integer mark;
    private final LocalDate date;
}
