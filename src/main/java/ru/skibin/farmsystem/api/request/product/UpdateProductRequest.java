package ru.skibin.farmsystem.api.request.product;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skibin.farmsystem.api.enumTypes.ValueType;

@Data
@AllArgsConstructor
public class UpdateProductRequest {
    @NotNull(message = "product name can't be null")
    @NotEmpty(message = "product name can't be empty")
    @Size(min = 2, max = 50, message = "product name: 2-50 chars")
    private String name;
    @NotNull(message = "value type can't be null")
    private ValueType valueType;
    private Boolean isActual;
}
