package ru.skibin.farmsystem.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skibin.farmsystem.api.data.enumTypes.ValueType;

@Data
@Schema(description = "Product response")
public class ProductResponse {
    @Schema(description = "Product numerical identifier")
    private final Long id;
    @Schema(description = "Product name")
    private final String name;
    @Schema(description = "KILOGRAM / LITER / PIECE")
    private final ValueType valueType;
    @Schema(description = "Product actuality status")
    private final Boolean isActual;
}
