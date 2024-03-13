package ru.skibin.farmsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skibin.farmsystem.api.enumTypes.Role;
import ru.skibin.farmsystem.api.request.profile.AddProfileRequest;
import ru.skibin.farmsystem.api.request.profile.UpdatePasswordRequest;
import ru.skibin.farmsystem.api.request.profile.UpdateProfileRequest;
import ru.skibin.farmsystem.api.response.ProfileResponse;
import ru.skibin.farmsystem.exception.common.ValidationException;
import ru.skibin.farmsystem.service.ProfileService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;

    /**
     * Adding new profile
     * @param addProfileRequest Request with new profile data
     * @param bindingResult     Request validation data
     * @return Http response with added profile response model
     */
    @Operation(summary = "adding new profile")
    @PostMapping
    public ResponseEntity<ProfileResponse> add(
            @Valid
            @RequestBody
            AddProfileRequest addProfileRequest,
            BindingResult bindingResult
    ) throws ValidationException
    {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        return new ResponseEntity<>(
                profileService.save(addProfileRequest),
                HttpStatus.OK
        );
    }

    /**
     * Getting profile
     * @param id Profile numerical identifier
     * @return Http response with profile response model
     */
    @Operation(summary = "Getting profile")
    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponse> get(
            @PathVariable("id")
            @Validated
            @Positive(message = "id must be positive")
            Long id
    ) {
        return new ResponseEntity<>(
                profileService.get(id),
                HttpStatus.OK
        );
    }

    /**
     * Getting profile with pagination
     * @param limit  Pagination limit
     * @param offset Pagination offset
     * @return Http response with collection of profile response models
     */
    @Operation(summary = "Getting profile with pagination")
    @GetMapping("/all")
    public ResponseEntity<Collection<ProfileResponse>> getAll(
            @RequestParam("limit")
            @Validated
            @PositiveOrZero(message = "limit must be positive")
            Integer limit,
            @RequestParam("offset")
            @Validated
            @PositiveOrZero(message = "offset must be positive")
            Integer offset
    ) {
        return new ResponseEntity<>(
                profileService.getAllWithPagination(limit, offset),
                HttpStatus.OK
        );
    }

    /**
     * Updating profile's information
     * @param id  Profile numerical identifier
     * @param fio fio: f - Surname, i - name, o - patronymic
     * @return Http response with updated profile response model
     */
    @Operation(summary = "Updating profile information")
    @PatchMapping("/{id}/info")
    public ResponseEntity<ProfileResponse> updateInfo(
            @PathVariable("id")
            @Validated
            @Positive(message = "id must be positive")
            Long id,
            @Validated
            @Size(min = 2, max = 50, message = "\"profile name size 2-50 chars\"")
            @RequestBody
            String fio
    ) {
        return new ResponseEntity<>(
                profileService.updateInf(id, fio),
                HttpStatus.OK
        );
    }

    /**
     * Updating profile's password
     * @param id                     Profile numerical identifier
     * @param updatePasswordRequest  Request with old & new password
     * @param bindingResult          Request validation data
     * @return Http response with updated profile response model
     */
    @Operation(summary = "Updating profile password")
    @PatchMapping("/{id}/password")
    public ResponseEntity<ProfileResponse> updatePassword(
            @PathVariable("id")
            @Validated
            @Positive(message = "id must be positive")
            Long id,
            @Valid
            @RequestBody
            UpdatePasswordRequest updatePasswordRequest,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        return new ResponseEntity<>(
                profileService.updatePassword(id, updatePasswordRequest),
                HttpStatus.OK
        );
    }

    /**
     * Updating profile's role
     * @param id   Profile numerical identifier
     * @param role Profile role
     * @return Http response with updated profile response model
     */
    @Operation(summary = "Updating profile's role")
    @PatchMapping("/{id}/role")
    public ResponseEntity<ProfileResponse> updateRole(
            @PathVariable("id")
            @Validated
            @Positive(message = "id must be positive")
            Long id,
            @RequestParam("role") Role role
    ) {
        return new ResponseEntity<>(
                profileService.updateRole(id, role),
                HttpStatus.OK
        );
    }

    /**
     * Updating profile's actuality status
     * @param id     Profile numerical identifier
     * @param status New actuality status for profile
     * @return Http response with updated profile response model
     */
    @Operation(summary = "Updating profile's role")
    @PatchMapping("/{id}/actuality")
    public ResponseEntity<ProfileResponse> updateActiveStatus(
            @PathVariable("id")
            @Validated
            @Positive
            Long id,
            @RequestParam("active") Boolean status
    ) {
        return new ResponseEntity<>(
                profileService.updateActualStatus(id, status),
                HttpStatus.OK
        );
    }

    /**
     * Updating profile
     * @param id                   Profile numerical identifier
     * @param updateProfileRequest Request with new data for profile
     * @param bindingResult        Request validation data
     * @return Http response with updated profile response model
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProfileResponse> updateProfile(
            @PathVariable("id")
            @Validated
            @Positive(message = "id must be positive")
            Long id,
            @Valid @RequestBody UpdateProfileRequest updateProfileRequest,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        return new ResponseEntity<>(
                profileService.update(id, updateProfileRequest),
                HttpStatus.OK
        );
    }

    /**
     * Deleting or deactivate profile
     * @param id Profile numerical identifier
     * @return true - if successful
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteProfile(
            @PathVariable("id")
            @Validated
            @Positive(message = "id must be positive")
            Long id
    ) {
        return new ResponseEntity<>(
                profileService.delete(id),
                HttpStatus.OK
        );
    }
}
