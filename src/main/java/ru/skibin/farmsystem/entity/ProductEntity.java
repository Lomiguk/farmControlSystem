package ru.skibin.farmsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skibin.farmsystem.api.data.enumTypes.ValueType;

@Data
@AllArgsConstructor
public class ProductEntity {
    private Long id;
    private String name;
    private ValueType valueType;
    private Boolean isActual;
}
