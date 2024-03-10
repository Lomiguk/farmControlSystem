package ru.skibin.farmsystem.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
import ru.skibin.farmsystem.api.dto.ProfileResponse;
import ru.skibin.farmsystem.api.request.profile.AddProfileRequest;
import ru.skibin.farmsystem.api.request.profile.UpdateInfoRequest;
import ru.skibin.farmsystem.api.request.profile.UpdatePasswordRequest;
import ru.skibin.farmsystem.api.request.profile.UpdateProfileRequest;
import ru.skibin.farmsystem.exception.common.ValidationException;
import ru.skibin.farmsystem.service.ProfileService;
import ru.skibin.farmsystem.util.BindingResultUtil;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping
    public ResponseEntity<ProfileResponse> add(
            @Valid
            @RequestBody
            AddProfileRequest addProfileRequest,
            BindingResult bindingResult
    ) throws ValidationException {

        if (bindingResult.hasErrors()) throw new ValidationException(
                BindingResultUtil.requestValidationToString(bindingResult)
        );

        return new ResponseEntity<>(
                profileService.add(
                        addProfileRequest.getFio(),
                        addProfileRequest.getEmail(),
                        addProfileRequest.getPassword(),
                        addProfileRequest.getIsAdmin()
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponse> get(
            @PathVariable("id")
            @Positive(message = "id must be positive")
            Long id
    ) {
        return new ResponseEntity<>(
                profileService.get(id),
                HttpStatus.OK
        );
    }

    @GetMapping("/all")
    public ResponseEntity<Collection<ProfileResponse>> getAll(
            @RequestParam("limit")
            @PositiveOrZero(message = "limit must be positive")
            Integer limit,
            @RequestParam("offset")
            @PositiveOrZero(message = "offset must be positive")
            Integer offset
    ) {
        return new ResponseEntity<>(
                profileService.getAllWithPagination(limit, offset),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{id}/info")
    public ResponseEntity<ProfileResponse> updateInfo(
            @PathVariable("id")
            @Positive(message = "id must be positive")
            Long id,
            @Valid
            @RequestBody
            UpdateInfoRequest updateInfoRequest,
            BindingResult bindingResult
    ) throws ValidationException {
        if (bindingResult.hasErrors()) throw new ValidationException(
                BindingResultUtil.requestValidationToString(bindingResult)
        );
        return new ResponseEntity<>(
                profileService.updateInf(
                        id,
                        updateInfoRequest.getFio(),
                        updateInfoRequest.getEmail()
                ),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<ProfileResponse> updatePassword(
            @PathVariable("id")
            @Positive(message = "id must be positive")
            Long id,
            @Valid
            @RequestBody
            UpdatePasswordRequest updatePasswordRequest,
            BindingResult bindingResult
    ) throws ValidationException {
        if (bindingResult.hasErrors()) throw new ValidationException(
                BindingResultUtil.requestValidationToString(bindingResult)
        );
        return new ResponseEntity<>(
                profileService.updatePassword(
                        id,
                        updatePasswordRequest.getOldPassword(),
                        updatePasswordRequest.getNewPassword()
                ),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{id}/admin")
    public ResponseEntity<ProfileResponse> updateAdminStatus(
            @PathVariable("id")
            @Positive(message = "id must be positive")
            Long id,
            @RequestParam("status") Boolean status
    ) {
        return new ResponseEntity<>(
                profileService.updateAdminStatus(
                        id,
                        status
                ),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{id}/active")
    public ResponseEntity<ProfileResponse> updateActiveStatus(
            @PathVariable("id") @Positive Long id,
            @RequestParam("active") Boolean status
    ) {
        return new ResponseEntity<>(
                profileService.updateActualStatus(
                        id,
                        status
                ),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileResponse> updateProfile(
            @PathVariable("id")
            @Positive(message = "id must be positive")
            Long id,
            @RequestBody UpdateProfileRequest updateProfileRequest
    ) {
        return new ResponseEntity<>(
                profileService.update(
                        id,
                        updateProfileRequest.getOldPassword(),
                        updateProfileRequest.getFio(),
                        updateProfileRequest.getEmail(),
                        updateProfileRequest.getNewPassword(),
                        updateProfileRequest.getIsAdmin(),
                        updateProfileRequest.getIsActive()
                ),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteProfile(
            @PathVariable("id")
            @Positive(message = "id must be positive")
            Long id
    ) {
        return new ResponseEntity<>(
                profileService.delete(id),
                HttpStatus.OK
        );
    }
}
