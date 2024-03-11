package ru.skibin.farmsystem.api.enumTypes;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Role {
    ADMIN("ADMIN"),
    USER("USER");

    final String name;
}
