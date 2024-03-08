package ru.skibin.farmsystem.api.request.profile;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdatePasswordRequest {
    String oldPassword;
    String newPassword;
}
