package ru.skibin.farmsystem.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.skibin.farmsystem.api.data.enumTypes.ValueType;

@Data
@RequiredArgsConstructor
public class ProductEntity {
    private final Long id;
    private final String name;
    private final ValueType valueType;
    private final Boolean isActual;
}