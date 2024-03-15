package ru.skibin.farmsystem.api.request.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.skibin.farmsystem.api.data.enumTypes.ValueType;

@Data
@RequiredArgsConstructor
@Schema(description = "Request with new product data")
public class AddProductRequest {
    @NotBlank(message = "Product name can't be empty")
    @Size(min = 2, max = 50, message = "Product name: 2-50 chars")
    @Schema(description = "Profile name")
    private final String name;
    @NotNull(message = "Value type can't be null")
    @Schema(description = "New value type")
    private final ValueType valueType;
}
