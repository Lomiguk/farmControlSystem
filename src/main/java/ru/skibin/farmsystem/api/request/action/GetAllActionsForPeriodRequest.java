package ru.skibin.farmsystem.api.request.action;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;


@Data
@AllArgsConstructor
public class GetAllActionsForPeriodRequest {
    @NotNull
    private LocalDate start;
    @NotNull
    private LocalDate  end;
}
