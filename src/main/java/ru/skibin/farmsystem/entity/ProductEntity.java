package ru.skibin.farmsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skibin.farmsystem.api.enumTypes.ValueType;

@Data
@AllArgsConstructor
public class ProductEntity {
    private Long id;
    private String name;
    private ValueType valueType;
}
