package ru.skibin.farmsystem.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skibin.farmsystem.api.enumTypes.ValueType;

@Data
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private ValueType valueType;
    private Boolean isActual;
}
