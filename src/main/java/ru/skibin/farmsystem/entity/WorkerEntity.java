package ru.skibin.farmsystem.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.skibin.farmsystem.api.data.enumTypes.Role;

@Data
@RequiredArgsConstructor
public class WorkerEntity {
    private final Long profileId;
    private final String profileFio;
    private final String profileEmail;
    private final Role profileRole;
}