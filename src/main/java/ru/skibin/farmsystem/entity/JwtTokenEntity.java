package ru.skibin.farmsystem.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.skibin.farmsystem.api.data.enumTypes.JwtType;

@Data
@RequiredArgsConstructor
public class JwtTokenEntity {
    private final Long id;
    private final Long profileId;
    private final String token;
    private final JwtType type;
}