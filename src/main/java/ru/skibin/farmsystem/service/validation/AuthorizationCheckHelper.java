package ru.skibin.farmsystem.service.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.skibin.farmsystem.entity.ProfileEntity;
import ru.skibin.farmsystem.exception.common.NonExistedProfileException;
import ru.skibin.farmsystem.repository.ProfileDAO;

@Component
@RequiredArgsConstructor
public class AuthorizationCheckHelper {
    private final String NON_PROFILE_EXCEPTION_MESSAGE = "Authorization: Non existed profile";

    private final ProfileDAO profileDAO;
    public ProfileEntity checkForExistedAuthorizedProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            Object principal = auth.getPrincipal();
            if (principal instanceof ProfileEntity) {
                ProfileEntity profileEntity = (ProfileEntity) auth.getPrincipal();
                return profileDAO.findProfileByEmail(profileEntity.getEmail());
            }
        }
        throw new NonExistedProfileException(NON_PROFILE_EXCEPTION_MESSAGE);
    }
}
