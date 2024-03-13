package ru.skibin.farmsystem.api.request.other;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.skibin.farmsystem.api.data.enumTypes.ValueType;

@Data
@RequiredArgsConstructor
@Schema(description = "Result of work")
public class WorkResult {
    @Schema(description = "Product id")
    private final Long productId;
    @Schema(description = "Product name")
    private final String productName;
    @Schema(description = "Product value type")
    private final ValueType productValueType;
    @Schema(description = "Product value")
    private final Float productValue;
}
