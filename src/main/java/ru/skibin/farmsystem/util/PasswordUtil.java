package ru.skibin.farmsystem.util;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class PasswordUtil {
    public static Long getHash(String origin) {
        return Hashing.sha256()
                .hashString(origin, StandardCharsets.UTF_8)
                .padToLong();
    }
}
