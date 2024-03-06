package ru.skibin.farmsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;

@Data
@AllArgsConstructor
public class ProfileEntity {
    Long id;
    String fio;
    String email;
    String password;
    Boolean isAdmin;
    Boolean isActual;
}
