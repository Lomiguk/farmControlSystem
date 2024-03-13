package ru.skibin.farmsystem.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skibin.farmsystem.api.enumTypes.ValueType;

import java.time.Instant;

@Data
@AllArgsConstructor
@Schema(description = "Response action")
public class ActionResponse {
    @Schema(description = "Action's numerical identifier")
    private Long id;
    @Schema(description = "Profile's numerical identifier")
    private Long profileId;
    @Schema(description = "Product's numerical identifier")
    private Long productId;
    @Schema(description = "Volume of collected product")
    private Float value;
    @Schema(description = "KILOGRAM / LITER / PIECE")
    private ValueType valueType;
    @Schema(description = "Action's registration time")
    private Instant time;
    @Schema(description = "Action's actuality status")
    boolean isActual;
}
