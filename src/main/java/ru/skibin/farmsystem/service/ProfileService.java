package ru.skibin.farmsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.skibin.farmsystem.api.data.enumTypes.Role;
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
    private static final Logger LOGGER = Logger.getLogger(ProductService.class.getName());
    private final ProfileDAO profileDAO;
    private final CommonCheckHelper commonCheckHelper;
    private final ProfileCheckHelper profileCheckHelper;
    private final EntityToResponseMapper entityMapper;

    /**
     * Adding profile to repository
     *
     * @param request Request with new profile data
     * @return saved profile
     */
    @Transactional
    public ProfileResponse add(BindingResult bindingResult, AddProfileRequest request) {
        commonCheckHelper
                .chainCheckValidation(bindingResult)
                .chainCheckForProfileEmailUnique(request.getEmail());

        String hashedPassword = new BCryptPasswordEncoder()
                .encode(PasswordUtil.getHash(request.getPassword()).toString());;

        Long id = profileDAO.add(
                request.getFio(),
                request.getEmail(),
                hashedPassword,
                request.getRole()
        );

        return get(id);
    }

    /**
     * Save entity to repository
     *
     * @param profileEntity new entity
     * @return saved profile
     */
    @Transactional
    public ProfileResponse add(ProfileEntity profileEntity) {
        commonCheckHelper.chainCheckForProfileEmailUnique(profileEntity.getEmail());

        Long id = profileDAO.add(
                profileEntity.getFio(),
                profileEntity.getEmail(),
                profileEntity.getPassword(),
                profileEntity.getRole()
        );

        return findProfile(id);
    }

    /**
     * Getting profile from repository
     *
     * @param id Profile numerical identifier
     * @return profile response model
     */
    @Transactional
    public ProfileResponse get(Long id) {
        commonCheckHelper
                .chainCheckAuthPermission(id)
                .checkProfileForActive(id);

        return findProfile(id);
    }

    public ProfileResponse findProfile(Long id) {
        return entityMapper.toResponse(profileDAO.findProfile(id));
    }

    /**
     * Updating profile
     *
     * @param id  Profile numerical identifier
     * @param fio F - Surname, I - name, O - patronymic
     * @return profile response model
     */
    @Transactional
    public ProfileResponse updateInf(Long id, String fio) {
        commonCheckHelper.checkProfileForActive(id);
        profileDAO.updateProfileInformation(id, fio);
        return findProfile(id);
    }

    /**
     * Updating profile password
     *
     * @param id      Profile numerical identifier
     * @param request Request with old & new passwor
     * @return profile response model
     */
    @Transactional
    public ProfileResponse updatePassword(BindingResult bindingResult, Long id, UpdatePasswordRequest request) {
        commonCheckHelper
                .chainCheckValidation(bindingResult)
                .chainCheckAuthPermission(id)
                .checkProfileForActive(id);
        profileCheckHelper.checkPasswords(id, request.getOldPassword(), request.getNewPassword());
        String newPassHash = PasswordUtil.getHash(request.getNewPassword()).toString();

        profileDAO.updatePassword(id, newPassHash);

        return findProfile(id);
    }

    /**
     * Updating profile admin status
     *
     * @param id   Profile numerical identifier
     * @param role flag - is admin profile
     * @return profile response model
     */
    @Transactional
    public ProfileResponse updateRole(Long id, Role role) {
        commonCheckHelper.checkProfileForActive(id);
        profileDAO.updateProfileRole(id, role);
        return findProfile(id);
    }

    /**
     * Updating profile active status
     *
     * @param id       Profile numerical identifier
     * @param isActual flag - is active profile
     * @return update profile live status
     */
    @Transactional
    public ProfileResponse updateActualStatus(Long id, Boolean isActual) {
        ProfileEntity profileEntity = commonCheckHelper
                .checkProfileForExist(id);
        if (isActual == null) isActual = profileEntity.getIsActual();
        profileDAO.updateProfileActualStatus(id, isActual);
        return findProfile(id);
    }

    /**
     * Deleting or deactivate profile
     *
     * @param id Profile numerical identifier
     * @return deleting status
     */
    @Transactional
    public Boolean delete(Long id) {
        commonCheckHelper.checkProfileForActive(id);
        if (commonCheckHelper.boolCheckProfileInActions(id)) {
            LOGGER.info(String.format("Try to delete profile (%d)", id));
            return profileDAO.deleteProfile(id) > 0;
        } else {
            LOGGER.info(String.format("Profile (%d) set actual status to false", id));
            updateActualStatus(id, false);
            return true;
        }
    }

    /**
     * Updating profile
     *
     * @param bindingResult  Request validation data
     * @param id             Profile numerical identifier
     * @param request        Request with new data for profile
     * @return profile response model
     */
    @Transactional
    public ProfileResponse update(
            BindingResult bindingResult,
            Long id,
            UpdateProfileRequest request
    ) {
        ProfileEntity profileEntity = commonCheckHelper
                .chainCheckValidation(bindingResult)
                .checkProfileForExist(id);
        profileCheckHelper
                .checkPasswords(id, request.getOldPassword(), request.getNewPassword());
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

        return findProfile(id);
    }

    /**
     * Getting profiles
     *
     * @param limit  pagination limit
     * @param offset pagination offset
     * @return profile response models
     */
    public Collection<ProfileResponse> getAllWithPagination(Integer limit, Integer offset) {
        Collection<ProfileResponse> profiles = new ArrayList<>();
        Collection<ProfileEntity> profileEntities = profileDAO.getAllProfileWithPagination(limit, offset);

        for (var profileEntity : profileEntities) {
            profiles.add(entityMapper.toResponse(profileEntity));
        }

        return profiles;
    }

    /**
     * Getting profile entity by email
     *
     * @param email email
     * @return profile response model entity
     */
    private ProfileEntity getProfileEntityByEmail(String email) {
        return commonCheckHelper.checkProfileForExistByEmail(email);
    }

    /**
     * Getting profile by email/login
     * <p>
     * Needed for Spring Security
     *
     * @return Profile as UserDetailsService
     */
    public UserDetailsService userDetailsService() {
        return this::getProfileEntityByEmail;
    }
}
