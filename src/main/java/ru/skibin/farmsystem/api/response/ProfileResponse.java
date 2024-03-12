package ru.skibin.farmsystem.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skibin.farmsystem.api.enumTypes.Role;

@Data
@AllArgsConstructor
public class ProfileResponse {
    Long id;
    String fio;
    String email;
    Role role;
    Boolean isActual;
}
