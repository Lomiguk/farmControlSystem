package ru.skibin.farmsystem.api.data.enumTypes;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ADMIN - all permissions
 * USER - creating/updating his actions
 */
@AllArgsConstructor
@Getter
public enum Role {
    ADMIN("ADMIN"),
    USER("USER");

    private final String name;
}
