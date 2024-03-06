package ru.skibin.farmsystem.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skibin.farmsystem.entity.ProfileEntity;

@Data
@AllArgsConstructor
public class ProfileDTO {
    String fio;
    String email;
    Boolean isAdmin;
    Boolean isActual;

    public ProfileDTO(ProfileEntity profileEntity) {
        this.fio = profileEntity.getFio();
        this.email = profileEntity.getEmail();
        this.isAdmin = profileEntity.getIsAdmin();
        this.isActual = profileEntity.getIsActual();
    }
}
