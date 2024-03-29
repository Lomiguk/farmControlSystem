package ru.skibin.farmsystem.api.request.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skibin.farmsystem.api.data.enumTypes.ValueType;

@Data
@AllArgsConstructor
@Schema(description = "Request with new data for product")
public class UpdateProductRequest {
    @NotBlank(message = "product name can't be empty")
    @Size(min = 2, max = 50, message = "product name: 2-50 chars")
    @Schema(description = "Product's new name")
    private String name;
    @NotNull(message = "Value type can't be null")
    @Schema(description = "Product's new value type")
    private ValueType valueType;
    @Schema(description = "Product's new status of actuality")
    private Boolean isActual;
}
