package ru.skibin.farmsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skibin.farmsystem.api.dto.ProfileResponse;
import ru.skibin.farmsystem.entity.ProfileEntity;
import ru.skibin.farmsystem.repository.ProfileDAO;
import ru.skibin.farmsystem.service.validation.CommonCheckHelper;
import ru.skibin.farmsystem.service.validation.ProfileCheckHelper;
import ru.skibin.farmsystem.util.PasswordUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileDAO profileDAO;
    private final Logger logger = Logger.getLogger(ProductService.class.getName());
    private final CommonCheckHelper commonCheckHelper;
    private final ProfileCheckHelper profileCheckHelper;
    @Transactional
    public ProfileResponse add(
            String fio,
            String email,
            String nonHashPas,
            Boolean isAdmin
    ) {
        long hash = PasswordUtil.getHash(nonHashPas);

        profileDAO.add(fio, email, String.valueOf(hash), isAdmin);

        ProfileEntity profileEntity = profileDAO.findProfile(fio, email);
        return new ProfileResponse(profileEntity);
    }

    @Transactional
    public ProfileResponse add(String fio, String email, String nonHashPas) {
        long hash = PasswordUtil.getHash(nonHashPas);

        profileDAO.add(fio, email, String.valueOf(hash));

        ProfileEntity profileEntity = profileDAO.findProfile(fio, email);
        return new ProfileResponse(profileEntity);
    }

    public ProfileResponse get(Long id) {
        commonCheckHelper.checkProfileForActive(id, "Non-existed profile for getting.");
        return new ProfileResponse(profileDAO.findProfile(id));
    }

    @Transactional
    public ProfileResponse updateInf(Long id, String fio, String email) {
        commonCheckHelper.checkProfileForActive(id, "Non-existed profile for update.");
        profileDAO.updateProfileInformation(id, fio, email);
        return new ProfileResponse(profileDAO.findProfile(id));
    }

    @Transactional
    public ProfileResponse updatePassword(Long id, String oldPassword, String newPassword) {
        commonCheckHelper.checkProfileForActive(id, "Non-existed profile for update status.");
        profileCheckHelper.checkPasswords(id, oldPassword, newPassword);
        String newPassHash = PasswordUtil.getHash(newPassword).toString();

        profileDAO.updatePassword(id, newPassHash);

        return new ProfileResponse(profileDAO.findProfile(id));
    }

    @Transactional
    public ProfileResponse updateAdminStatus(Long id, Boolean isAdmin) {
        commonCheckHelper.checkProfileForActive(id, "Non-existed profile for update admin status.");
        profileDAO.updateProfileAdminStatus(id, isAdmin);
        return new ProfileResponse(profileDAO.findProfile(id));
    }

    @Transactional
    public ProfileResponse updateActualStatus(Long id, Boolean isActual) {
        ProfileEntity profileEntity = commonCheckHelper.checkProfileForExist(id, "Non-existed profile for update actual status.");
        if (isActual == null) isActual = profileEntity.getIsActual();
        profileDAO.updateProfileActualStatus(id, isActual);
        return new ProfileResponse(profileDAO.findProfile(id));
    }

    @Transactional
    public Boolean delete(Long id) {
        commonCheckHelper.checkProfileForActive(id, "Non-existed profile for delete.");
        if (commonCheckHelper.boolCheckProfileInActions(id, "Non-deletable (has dependent actions)")) {
            logger.info("Try to delete profile (" + id + ")");
            return profileDAO.deleteProfile(id) > 0;
        } else {
            logger.info("Profile (" + id + ") set actual status to false");
            updateActualStatus(id, false);
            return true;
        }
    }

    @Transactional
    public ProfileResponse update(
            Long id,
            String oldPassword,
            String newFio,
            String newEmail,
            String newPassword,
            Boolean isAdmin,
            Boolean isActual
    ) {
        ProfileEntity profileEntity = commonCheckHelper.checkProfileForExist(id, "Non-existed profile for update.");
        profileCheckHelper.checkPasswords(id, oldPassword, newPassword);
        String newPassHash = PasswordUtil.getHash(newPassword).toString();

        if (isAdmin == null) isAdmin = profileEntity.getIsAdmin();
        if (isActual == null) isActual = profileEntity.getIsActual();

        profileDAO.updateProfile(
                id,
                newFio,
                newEmail,
                newPassHash,
                isAdmin,
                isActual
        );

        return new ProfileResponse(profileDAO.findProfile(id));
    }

    public Collection<ProfileResponse> getAllWithPagination(Integer limit, Integer offset) {
        Collection<ProfileResponse> profiles = new ArrayList<>();
        Collection<ProfileEntity> profileEntities = profileDAO.getAllProfileWithPagination(limit, offset);

        for (var profileEntity : profileEntities) {
            profiles.add(new ProfileResponse(profileEntity));
        }

        return profiles;
    }
}
