package ru.skibin.farmsystem.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skibin.farmsystem.api.enumTypes.ValueType;
import ru.skibin.farmsystem.entity.ActionEntity;

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

    public ActionResponse(ActionEntity actionEntity, ValueType valueType) {
        this.id = actionEntity.getId();
        this.profileId = actionEntity.getProfileId();
        this.productId = actionEntity.getProductId();
        this.valueType = valueType;
        this.value = actionEntity.getValue();
        this.time = actionEntity.getTime();
        this.isActual = actionEntity.getIsActual();
    }
}
