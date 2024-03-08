package ru.skibin.farmsystem.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skibin.farmsystem.api.enumTypes.ValueType;
import ru.skibin.farmsystem.entity.ProductEntity;

@Data
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private ValueType valueType;

    public ProductResponse(ProductEntity productEntity) {
        this.id = productEntity.getId();
        this.name = productEntity.getName();
        this.valueType = productEntity.getValueType();
    }
}
