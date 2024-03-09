package ru.skibin.farmsystem.service.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.skibin.farmsystem.entity.ProfileEntity;
import ru.skibin.farmsystem.exception.profile.UpdatePasswordException;
import ru.skibin.farmsystem.repository.ProfileDAO;
import ru.skibin.farmsystem.util.PasswordUtil;

@Component
@RequiredArgsConstructor
public class ProfileCheckHelper {
    private final ProfileDAO profileDAO;
    public void checkPasswords(Long id, String oldPassword, String newPassword) {
        ProfileEntity profileEntity = profileDAO.findProfile(id);

        String oldPassHash = PasswordUtil.getHash(oldPassword).toString();

        if (oldPassword.equals(newPassword))
            throw new UpdatePasswordException("Old password is equal to the new one");
        if (!profileEntity.getPassword().equals(oldPassHash))
            throw new UpdatePasswordException("Wrong old password");
    }
}
