package ru.skibin.farmsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;


@Data
@AllArgsConstructor
public class ActionEntity {
    private Long id;
    private Long profileId;
    private Long productId;
    private Float value;
    private Instant time;
    private Boolean isActual;
}
