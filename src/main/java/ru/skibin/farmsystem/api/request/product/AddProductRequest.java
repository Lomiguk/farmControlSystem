package ru.skibin.farmsystem.api.request.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.skibin.farmsystem.api.data.enumTypes.ValueType;

@Data
@RequiredArgsConstructor
@Schema(description = "Request with new product data")
public class AddProductRequest {
    @NotNull(message = "product name can't be null")
    @NotEmpty(message = "product name can't be empty")
    @Size(min = 2, max = 50, message = "product name: 2-50 chars")
    @Schema(description = "Profile name")
    private String name;
    @NotNull(message = "Value type can't be null")
    @Schema(description = "New value type")
    private ValueType valueType;
    @Schema(description = "Boolean product actuality status")
    private boolean isActual;
}
