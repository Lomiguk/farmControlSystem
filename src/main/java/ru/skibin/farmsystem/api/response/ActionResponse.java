package ru.skibin.farmsystem.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skibin.farmsystem.api.enumTypes.ValueType;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ActionResponse {
    Long id;
    Long profileId;
    Long productId;
    Float value;
    ValueType valueType;
    Instant time;
    boolean isActual;
}
