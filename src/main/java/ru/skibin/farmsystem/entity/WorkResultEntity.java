package ru.skibin.farmsystem.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.skibin.farmsystem.api.data.enumTypes.ValueType;

@Data
@RequiredArgsConstructor
public class WorkResultEntity {
    private final Long productId;
    private final String productName;
    private final ValueType productValueType;
    private final Float productValue;
}