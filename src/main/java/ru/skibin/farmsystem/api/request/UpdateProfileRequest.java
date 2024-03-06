package ru.skibin.farmsystem.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateProfileRequest {
    String oldPassword;
    String fio;
    String email;
    String newPassword;
    Boolean isAdmin;
    Boolean isActive;
}
