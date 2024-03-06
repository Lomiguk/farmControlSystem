package ru.skibin.farmsystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//TODO(disable *)
import org.springframework.web.bind.annotation.*;
import ru.skibin.farmsystem.api.dto.ProfileDTO;
import ru.skibin.farmsystem.api.request.AddProfileRequest;
import ru.skibin.farmsystem.api.request.UpdateInfoRequest;
import ru.skibin.farmsystem.api.request.UpdatePasswordRequest;
import ru.skibin.farmsystem.api.request.UpdateProfileRequest;
import ru.skibin.farmsystem.service.ProfileService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;
    @PostMapping
    public ResponseEntity<ProfileDTO> add(
            @Valid @RequestBody AddProfileRequest addProfileRequest
    ) {
        return new ResponseEntity<>(
                profileService.add(
                        addProfileRequest.getName(),
                        addProfileRequest.getEmail(),
                        addProfileRequest.getPassword(),
                        addProfileRequest.getIsAdmin()
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileDTO> get(
            @PathVariable("id") Long id
    ) {
        return new ResponseEntity<>(
                profileService.get(id),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{id}/info")
    public ResponseEntity<ProfileDTO> updateInfo(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateInfoRequest updateInfoRequest
    ) {
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
    public ResponseEntity<ProfileDTO> updatePassword(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest
    ) {
        return new ResponseEntity<>(
                profileService.updatePassword(
                        id,
                        updatePasswordRequest.getOldPassword(),
                        updatePasswordRequest.getNewPassword()
                ),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{id}/admin?status=?")
    public ResponseEntity<ProfileDTO> updateAdminStatus(
        @PathVariable("id") Long id,
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
    @PatchMapping("/{id}/active?status=?")
    public ResponseEntity<ProfileDTO> updateActiveStatus(
            @PathVariable("id") Long id,
            @RequestParam("active") Boolean status
    ) {
        return new ResponseEntity<>(
                profileService.updateActiveStatus(
                        id,
                        status
                ),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileDTO> updateProfile(
        @PathVariable("id") Long id,
        @RequestBody UpdateProfileRequest updateProfileRequest
    ){
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
            @PathVariable("id") Long id
    ) {
        profileService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
