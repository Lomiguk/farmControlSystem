package ru.skibin.farmsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
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

    /**
     * Adding profile to repository
     * @param fio F - Surname, i - name, o - patronymic
     * @param email email/login
     * @param nonHashPas non-hashed password
     * @param isAdmin flag - is admin profile
     * @return saved profile
     */
    @Transactional
    public ProfileResponse save(
            String fio,
            String email,
            String nonHashPas,
            Boolean isAdmin
    ) {
        commonCheckHelper.chainCheckForProfileEmailUnique(email, "Profile with that email already exist");

        long hash = PasswordUtil.getHash(nonHashPas);

        profileDAO.add(fio, email, String.valueOf(hash), isAdmin);

        ProfileEntity profileEntity = profileDAO.findProfile(fio, email);
        return new ProfileResponse(profileEntity);
    }

    /**
     * Adding profile to repository with default non-admin status
     * @param fio F - Surname, I - name, O - patronymic
     * @param email email/login
     * @param nonHashPas non-hashed password
     * @return saved profile
     */
    @Transactional
    public ProfileResponse save(String fio, String email, String nonHashPas) {
        commonCheckHelper.chainCheckForProfileEmailUnique(email, "Profile with that email already exist");

        long hash = PasswordUtil.getHash(nonHashPas);

        profileDAO.add(fio, email, String.valueOf(hash));

        ProfileEntity profileEntity = profileDAO.findProfile(fio, email);
        return new ProfileResponse(profileEntity);
    }

    /**
     * Save entity to repository
     * @param profileEntity new entity
     * @return saved profile
     */
    @Transactional
    public ProfileResponse save(ProfileEntity profileEntity) {
        commonCheckHelper.chainCheckForProfileEmailUnique(profileEntity.getEmail(), "Profile with that email already exist");

        profileDAO.add(
                profileEntity.getFio(),
                profileEntity.getEmail(),
                profileEntity.getPassword(),
                profileEntity.getIsAdmin()
        );

        profileEntity = profileDAO.findProfile(profileEntity.getFio(), profileEntity.getEmail());
        return new ProfileResponse(profileEntity);
    }


    /**
     * Getting profile from repository
     * @param id unique numerical identifier
     * @return searched profile
     */
    public ProfileResponse get(Long id) {
        commonCheckHelper.checkProfileForActive(id, "Non-existed profile for getting.");
        return new ProfileResponse(profileDAO.findProfile(id));
    }

    /**
     * Updating profile
     * @param id  unique numerical identifier
     * @param fio F - Surname, I - name, O - patronymic
     * @return updated profile
     */
    @Transactional
    public ProfileResponse updateInf(Long id, String fio) {
        commonCheckHelper.checkProfileForActive(id, "Non-existed profile for update.");
        profileDAO.updateProfileInformation(id, fio);
        return new ProfileResponse(profileDAO.findProfile(id));
    }

    /**
     * Updating profile password
     * @param id unique numerical identifier
     * @param oldPassword old password
     * @param newPassword new password
     * @return updated profile
     */
    @Transactional
    public ProfileResponse updatePassword(Long id, String oldPassword, String newPassword) {
        commonCheckHelper.checkProfileForActive(id, "Non-existed profile for update status.");
        profileCheckHelper.checkPasswords(id, oldPassword, newPassword);
        String newPassHash = PasswordUtil.getHash(newPassword).toString();

        profileDAO.updatePassword(id, newPassHash);

        return new ProfileResponse(profileDAO.findProfile(id));
    }

    /**
     * Updating profile admin status
     * @param id unique numerical identifier
     * @param isAdmin flag - is admin profile
     * @return updated profile
     */
    @Transactional
    public ProfileResponse updateAdminStatus(Long id, Boolean isAdmin) {
        commonCheckHelper.checkProfileForActive(id, "Non-existed profile for update admin status.");
        profileDAO.updateProfileAdminStatus(id, isAdmin);
        return new ProfileResponse(profileDAO.findProfile(id));
    }

    /**
     * Updating profile active status
     * @param id unique numerical identifier
     * @param isActual flag - is active profile
     * @return update profile live status
     */
    @Transactional
    public ProfileResponse updateActualStatus(Long id, Boolean isActual) {
        ProfileEntity profileEntity = commonCheckHelper.checkProfileForExist(id, "Non-existed profile for update actual status.");
        if (isActual == null) isActual = profileEntity.getIsActual();
        profileDAO.updateProfileActualStatus(id, isActual);
        return new ProfileResponse(profileDAO.findProfile(id));
    }

    /**
     * Deleting or deactivate profile
     * @param id unique numerical identifier
     * @return deleting status
     */
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

    /**
     * Updating profile
     * @param id unique numerical identifier
     * @param oldPassword old password
     * @param newFio new F - Surname, I - name, O - patronymic
     * @param newEmail new email
     * @param newPassword new password
     * @param isAdmin flag - is admin profile
     * @param isActual flag - is active profile
     * @return updated profile
     */
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

    /**
     * Getting profiles
     * @param limit count of profiles
     * @param offset table offset
     * @return searched profiles
     */
    public Collection<ProfileResponse> getAllWithPagination(Integer limit, Integer offset) {
        Collection<ProfileResponse> profiles = new ArrayList<>();
        Collection<ProfileEntity> profileEntities = profileDAO.getAllProfileWithPagination(limit, offset);

        for (var profileEntity : profileEntities) {
            profiles.add(new ProfileResponse(profileEntity));
        }

        return profiles;
    }

    /**
     * Getting profile entity by email
     * @param email email
     * @return searched profile entity
     */
    private ProfileEntity getProfileEntityByEmail(String email) {
        return  commonCheckHelper.checkProfileForExistByEmail(email, String.format("Profile %s is not found", email));
    }

    /**
     * Getting profile by email
     * @param email email
     * @return searched profile
     */
    public ProfileResponse getProfileByEmail(String email) {
        return new ProfileResponse(
                commonCheckHelper.checkProfileForExistByEmail(email, String.format("Profile %s is not found", email))
        );
    }

    /**
     * Getting profile by email/login
     * <p>
     * Needed for Spring Security
     * @return пользователь
     */
    public UserDetailsService userDetailsService() {
        return this::getProfileEntityByEmail;
    }

    /**
     * Getting current profile
     * @return current profile
     */
    public ProfileResponse getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        var login = SecurityContextHolder.getContext().getAuthentication().getName();
        return getProfileByEmail(login);
    }
}
