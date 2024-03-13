package ru.skibin.farmsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skibin.farmsystem.api.enumTypes.Role;
import ru.skibin.farmsystem.api.request.profile.AddProfileRequest;
import ru.skibin.farmsystem.api.request.profile.UpdatePasswordRequest;
import ru.skibin.farmsystem.api.request.profile.UpdateProfileRequest;
import ru.skibin.farmsystem.api.response.ProfileResponse;
import ru.skibin.farmsystem.entity.ProfileEntity;
import ru.skibin.farmsystem.repository.ProfileDAO;
import ru.skibin.farmsystem.service.mapper.EntityToResponseMapper;
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
    private final EntityToResponseMapper entityMapper;

    /**
     * Adding profile to repository
     * @param request Request with new profile data
     * @return saved profile
     */
    @Transactional
    public ProfileResponse save(AddProfileRequest request) {
        commonCheckHelper.chainCheckForProfileEmailUnique(
                request.getEmail(),
                "Profile with that email already exist"
        );

        long hash = PasswordUtil.getHash(request.getPassword());

        Long id = profileDAO.add(
                request.getFio(),
                request.getEmail(),
                String.valueOf(hash),
                request.getRole()
        );

        ProfileEntity profileEntity = profileDAO.findProfile(id);
        return entityMapper.profileToResponse(profileEntity);
    }

    /**
     * Save entity to repository
     * @param profileEntity new entity
     * @return saved profile
     */
    @Transactional
    public ProfileResponse save(ProfileEntity profileEntity) {
        commonCheckHelper.chainCheckForProfileEmailUnique(profileEntity.getEmail(), "Profile with that email already exist");

        Long id = profileDAO.add(
                profileEntity.getFio(),
                profileEntity.getEmail(),
                profileEntity.getPassword(),
                profileEntity.getRole()
        );

        profileEntity = profileDAO.findProfile(id);
        return entityMapper.profileToResponse(profileEntity);
    }


    /**
     * Getting profile from repository
     * @param id Profile numerical identifier
     * @return profile response model
     */
    public ProfileResponse get(Long id) {
        commonCheckHelper
                .checkAuthPermission(id)
                .checkProfileForActive(id, "Non-existed profile for getting.");

        return entityMapper.profileToResponse(profileDAO.findProfile(id));
    }

    /**
     * Updating profile
     * @param id  Profile numerical identifier
     * @param fio F - Surname, I - name, O - patronymic
     * @return profile response model
     */
    @Transactional
    public ProfileResponse updateInf(Long id, String fio) {
        commonCheckHelper.checkProfileForActive(id, "Non-existed profile for update.");
        profileDAO.updateProfileInformation(id, fio);
        return entityMapper.profileToResponse(profileDAO.findProfile(id));
    }

    /**
     * Updating profile password
     * @param id       Profile numerical identifier
     * @param request  Request with old & new passwor
     * @return profile response model
     */
    @Transactional
    public ProfileResponse updatePassword(Long id, UpdatePasswordRequest request) {
        commonCheckHelper
                .checkAuthPermission(id)
                .checkProfileForActive(id, "Non-existed profile for update status.");
        profileCheckHelper.checkPasswords(id, request.getOldPassword(), request.getNewPassword());
        String newPassHash = PasswordUtil.getHash(request.getNewPassword()).toString();

        profileDAO.updatePassword(id, newPassHash);

        return entityMapper.profileToResponse(profileDAO.findProfile(id));
    }

    /**
     * Updating profile admin status
     * @param id Profile numerical identifier
     * @param role flag - is admin profile
     * @return profile response model
     */
    @Transactional
    public ProfileResponse updateRole(Long id, Role role) {
        commonCheckHelper.checkProfileForActive(id, "Non-existed profile for update admin status.");
        profileDAO.updateProfileRole(id, role);
        return entityMapper.profileToResponse(profileDAO.findProfile(id));
    }

    /**
     * Updating profile active status
     * @param id Profile numerical identifier
     * @param isActual flag - is active profile
     * @return update profile live status
     */
    @Transactional
    public ProfileResponse updateActualStatus(Long id, Boolean isActual) {
        ProfileEntity profileEntity = commonCheckHelper.checkProfileForExist(id, "Non-existed profile for update actual status.");
        if (isActual == null) isActual = profileEntity.getIsActual();
        profileDAO.updateProfileActualStatus(id, isActual);
        return entityMapper.profileToResponse(profileDAO.findProfile(id));
    }

    /**
     * Deleting or deactivate profile
     * @param id Profile numerical identifier
     * @return deleting status
     */
    @Transactional
    public Boolean delete(Long id) {
        commonCheckHelper.checkProfileForActive(id, "Non-existed profile for delete.");
        if (commonCheckHelper.boolCheckProfileInActions(id, "Non-deletable (has dependent actions)")) {
            logger.info(String.format("Try to delete profile (%d)", id));
            return profileDAO.deleteProfile(id) > 0;
        } else {
            logger.info(String.format("Profile (%d) set actual status to false", id));
            updateActualStatus(id, false);
            return true;
        }
    }

    /**
     * Updating profile
     * @param id Profile numerical identifier
     * @param request Request with new data for profile
     * @return profile response model
     */
    @Transactional
    public ProfileResponse update(
            Long id,
            UpdateProfileRequest request
    ) {
        ProfileEntity profileEntity = commonCheckHelper.checkProfileForExist(id, "Non-existed profile for update.");
        profileCheckHelper.checkPasswords(id, request.getOldPassword(), request.getNewPassword());
        String newPassHash = PasswordUtil.getHash(request.getNewPassword()).toString();

        Role newRole = request.getRole() == null ? profileEntity.getRole() : request.getRole();
        Boolean isActual = request.getIsActual() == null ? profileEntity.getIsActual() : request.getIsActual();

        profileDAO.updateProfile(
                id,
                request.getFio(),
                newPassHash,
                newRole,
                isActual
        );

        return entityMapper.profileToResponse(profileDAO.findProfile(id));
    }

    /**
     * Getting profiles
     * @param limit  pagination limit
     * @param offset pagination offset
     * @return profile response models
     */
    public Collection<ProfileResponse> getAllWithPagination(Integer limit, Integer offset) {
        Collection<ProfileResponse> profiles = new ArrayList<>();
        Collection<ProfileEntity> profileEntities = profileDAO.getAllProfileWithPagination(limit, offset);

        for (var profileEntity : profileEntities) {
            profiles.add(entityMapper.profileToResponse(profileEntity));
        }

        return profiles;
    }

    /**
     * Getting profile entity by email
     * @param email email
     * @return profile response model entity
     */
    private ProfileEntity getProfileEntityByEmail(String email) {
        return  commonCheckHelper.checkProfileForExistByEmail(email, String.format("Profile %s is not found", email));
    }

    /**
     * Getting profile by email/login
     * <p>
     * Needed for Spring Security
     * @return Profile as UserDetailsService
     */
    public UserDetailsService userDetailsService() {
        return this::getProfileEntityByEmail;
    }

}
