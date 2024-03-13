package ru.skibin.farmsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skibin.farmsystem.api.data.enumTypes.ValueType;

@Data
@AllArgsConstructor
public class WorkResultEntity {
    private final Long productId;
    private final String productName;
    private final ValueType productValueType;
    private final Float productValue;
}
