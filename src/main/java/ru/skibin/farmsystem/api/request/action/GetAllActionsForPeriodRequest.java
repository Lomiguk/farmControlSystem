package ru.skibin.farmsystem.api.request.action;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;


@Data
@AllArgsConstructor
public class GetAllActionsForPeriodRequest {
    @NotNull
    Date start;
    @NotNull
    Date end;
}
