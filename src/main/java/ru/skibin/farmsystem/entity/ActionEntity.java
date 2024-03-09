package ru.skibin.farmsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;


@Data
@AllArgsConstructor
public class ActionEntity {
    Long id;
    Long profileId;
    Long productId;
    Float value;
    Instant time;
    Boolean isActual;
}
