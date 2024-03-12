package ru.skibin.farmsystem.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skibin.farmsystem.api.enumTypes.Role;
import ru.skibin.farmsystem.entity.ProfileEntity;

@Data
@AllArgsConstructor
public class ProfileResponse {
    Long id;
    String fio;
    String email;
    Role role;
    Boolean isActual;

    public ProfileResponse(ProfileEntity profileEntity) {
        this.id = profileEntity.getId();
        this.fio = profileEntity.getFio();
        this.email = profileEntity.getEmail();
        this.role = profileEntity.getRole();
        this.isActual = profileEntity.getIsActual();
    }
}
