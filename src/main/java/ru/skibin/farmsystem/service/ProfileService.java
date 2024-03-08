package ru.skibin.farmsystem.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skibin.farmsystem.api.dto.ProfileResponse;
import ru.skibin.farmsystem.entity.ProfileEntity;
import ru.skibin.farmsystem.exception.common.WrongLimitOffsetException;
import ru.skibin.farmsystem.exception.profile.UpdatePasswordException;
import ru.skibin.farmsystem.repository.ProfileDAO;
import ru.skibin.farmsystem.util.PasswordUtil;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileDAO profileDAO;

    @Transactional
    public ProfileResponse add(
            @Valid String fio,
            @Valid String email,
            @Valid String nonHashPas,
            Boolean isAdmin
    ) {
        long hash = PasswordUtil.getHash(nonHashPas);

        profileDAO.add(fio, email, String.valueOf(hash), isAdmin);

        ProfileEntity profileEntity = profileDAO.getProfile(fio, email);
        return new ProfileResponse(profileEntity);
    }

    @Transactional
    public ProfileResponse add(
            @Valid String fio,
            @Valid String email,
            @Valid String nonHashPas
    ) {
        long hash = PasswordUtil.getHash(nonHashPas);

        profileDAO.add(fio, email, String.valueOf(hash));

        ProfileEntity profileEntity = profileDAO.getProfile(fio, email);
        return new ProfileResponse(profileEntity);
    }

    public ProfileResponse get(Long id) {
        return new ProfileResponse(profileDAO.getProfile(id));
    }

    @Transactional
    public ProfileResponse updateInf(Long id, String fio, String email) {
        profileDAO.updateProfileInformation(id, fio, email);
        return new ProfileResponse(profileDAO.getProfile(id));
    }

    @Transactional
    public ProfileResponse updatePassword(Long id, String oldPassword, String newPassword) {
        checkPasswords(id, oldPassword, newPassword);
        String newPassHash = PasswordUtil.getHash(newPassword).toString();

        profileDAO.updatePassword(id, newPassHash);

        return new ProfileResponse(profileDAO.getProfile(id));
    }

    @Transactional
    public ProfileResponse updateAdminStatus(Long id, Boolean isAdmin) {
        profileDAO.updateProfileAdminStatus(id, isAdmin);
        return new ProfileResponse(profileDAO.getProfile(id));
    }

    @Transactional
    public ProfileResponse updateActiveStatus(Long id, Boolean isActive) {
        profileDAO.updateProfileActiveStatus(id, isActive);
        return new ProfileResponse(profileDAO.getProfile(id));
    }

    public void delete(Long id) {
        profileDAO.deleteProfile(id);
    }

    @Transactional
    public ProfileResponse update(
            Long id,
            String oldPassword,
            String newFio,
            String newEmail,
            String newPassword,
            Boolean isAdmin,
            Boolean isActive
    ) {
        checkPasswords(id, oldPassword, newPassword);
        String newPassHash = PasswordUtil.getHash(newPassword).toString();

        profileDAO.updateProfile(
                id,
                newFio,
                newEmail,
                newPassHash,
                isAdmin,
                isActive
        );

        return new ProfileResponse(profileDAO.getProfile(id));
    }

    private void checkPasswords(Long id, String oldPassword, String newPassword) {
        ProfileEntity profileEntity = profileDAO.getProfile(id);

        String oldPassHash = PasswordUtil.getHash(oldPassword).toString();

        if (oldPassword.equals(newPassword))
            throw new UpdatePasswordException("Old password is equal to the new one");
        if (!profileEntity.getPassword().equals(oldPassHash))
            throw new UpdatePasswordException("Wrong old password");
    }

    public Collection<ProfileResponse> getAllWithPagination(Integer limit, Integer offset) {
        if (limit < 0 || offset < 0) throw new WrongLimitOffsetException("Wrong limit/offset values.");

        Collection<ProfileResponse> profiles = new ArrayList<>();
        Collection<ProfileEntity> profileEntities = profileDAO.getAllProfileWithPagination(limit, offset);

        for (var profileEntity : profileEntities) {
            profiles.add(new ProfileResponse(profileEntity));
        }

        return profiles;
    }
}
