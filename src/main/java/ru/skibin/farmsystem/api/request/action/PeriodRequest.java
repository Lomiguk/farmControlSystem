package ru.skibin.farmsystem.api.request.action;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@Schema(description = "Request for getting actions by period of time")
public class PeriodRequest {
    @NotNull
    @Schema(description = "Start of the time period")
    private LocalDate start;
    @NotNull
    @Schema(description = "End of the time period")
    private LocalDate  end;
}
