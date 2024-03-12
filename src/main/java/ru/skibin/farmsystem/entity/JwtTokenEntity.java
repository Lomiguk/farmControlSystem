package ru.skibin.farmsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skibin.farmsystem.api.enumTypes.JwtType;

@Data
@AllArgsConstructor
public class JwtTokenEntity {
    private Long id;
    private Long profileId;
    private String token;
    private JwtType type;
}
