package ru.skibin.farmsystem.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.skibin.farmsystem.api.data.enumTypes.Role;
import ru.skibin.farmsystem.api.data.enumTypes.ValueType;

@Data
@RequiredArgsConstructor
public class StatisticRow {
    private final Long profileId;
    private final String profileFio;
    private final String profileEmail;
    private final Role profileRole;
    private final Long productId;
    private final String productName;
    private final ValueType productValueType;
    private final Float productValue;
}