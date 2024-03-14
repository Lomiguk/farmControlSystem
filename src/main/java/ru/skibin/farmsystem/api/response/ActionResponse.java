package ru.skibin.farmsystem.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.skibin.farmsystem.api.data.enumTypes.ValueType;

import java.time.Instant;

@Data
@Schema(description = "Response action")
@RequiredArgsConstructor
public class ActionResponse {
    @Schema(description = "Action's numerical identifier")
    private final Long id;
    @Schema(description = "Profile's numerical identifier")
    private final Long profileId;
    @Schema(description = "Product's numerical identifier")
    private final Long productId;
    @Schema(description = "Volume of collected product")
    private final Float value;
    @Schema(description = "KILOGRAM / LITER / PIECE")
    private final ValueType valueType;
    @Schema(description = "Action's registration time")
    private final Instant time;
    @Schema(description = "Action's actuality status")
    private final boolean isActual;
}
