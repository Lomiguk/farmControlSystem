package ru.skibin.farmsystem.api.data.enumTypes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtType {
    ACCESS("ACCESS"),
    REFRESH("REFRESH");

    private final String name;

}
