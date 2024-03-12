package ru.skibin.farmsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skibin.farmsystem.api.enumTypes.JwtType;

@Data
@AllArgsConstructor
public class JwtTokenEntity {
    Long id;
    Long profileId;
    String token;
    JwtType type;
}
