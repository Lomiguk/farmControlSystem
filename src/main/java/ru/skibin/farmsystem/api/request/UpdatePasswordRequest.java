package ru.skibin.farmsystem.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdatePasswordRequest {
    String oldPassword;
    String newPassword;
}
