package ru.skibin.farmsystem.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skibin.farmsystem.api.enumTypes.ValueType;

@Data
@AllArgsConstructor
@Schema(description = "Product response")
public class ProductResponse {
    @Schema(description = "Product numerical identifier")
    private Long id;
    @Schema(description = "Product name")
    private String name;
    @Schema(description = "KILOGRAM / LITER / PIECE")
    private ValueType valueType;
    @Schema(description = "Product actuality status")
    private Boolean isActual;
}
