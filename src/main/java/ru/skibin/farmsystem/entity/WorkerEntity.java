package ru.skibin.farmsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skibin.farmsystem.api.data.enumTypes.Role;

@Data
@AllArgsConstructor
public class WorkerEntity {
    private final Long profileId;
    private final String profileFio;
    private final String profileEmail;
    private final Role profileRole;
}