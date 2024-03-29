package ru.skibin.farmsystem.service.validation;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.skibin.farmsystem.entity.ProfileEntity;
import ru.skibin.farmsystem.exception.common.NonExistedEntityException;
import ru.skibin.farmsystem.repository.JwtDAO;
import ru.skibin.farmsystem.repository.ProfileDAO;

import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class AuthorizationCheckHelper {
    private static final String NON_PROFILE_EXCEPTION_MESSAGE = "Authorization: Non existed profile";
    private static final String WRONG_SIGNATURE_MESSAGE = "A token with an incorrect signature was received";
    private static final String EXPIRED_JWT_MESSAGE = "Expired token received";
    private static final String UNEXPECTED_ERROR_MESSAGE = "Unexpected error while validating JWT";
    private static final String NON_EXISTED_TOKEN_MESSAGE = "Non-existed token";
    private static final Logger LOGGER = Logger.getLogger(AuthorizationCheckHelper.class.getName());

    private final ProfileDAO profileDAO;
    private final JwtDAO jwtDAO;

    public ProfileEntity checkForExistedAuthorizedProfileFromContext() {
        return checkForExistedAuthorizedProfile(
                SecurityContextHolder.getContext().getAuthentication()
        );
    }

    public ProfileEntity checkForExistedAuthorizedProfile(Authentication auth) {
        if (auth != null) {
            Object principal = auth.getPrincipal();
            if (principal instanceof ProfileEntity) {
                ProfileEntity profileEntity = (ProfileEntity) auth.getPrincipal();
                return profileDAO.findProfileByEmail(profileEntity.getEmail());
            }
        }
        throw new NonExistedEntityException(NON_PROFILE_EXCEPTION_MESSAGE);
    }

    public boolean boolCheckTokenForValidation(String token, Key key) {
        try {
            if (jwtDAO.findToken(token) == null) {
                LOGGER.info(NON_EXISTED_TOKEN_MESSAGE);
                throw new AccessDeniedException(NON_EXISTED_TOKEN_MESSAGE);
            }

            Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            LOGGER.log(Level.INFO, WRONG_SIGNATURE_MESSAGE, e);
            throw new AccessDeniedException(WRONG_SIGNATURE_MESSAGE);
        } catch (ExpiredJwtException e) {
            LOGGER.log(Level.INFO, EXPIRED_JWT_MESSAGE, e);
            throw new AccessDeniedException(EXPIRED_JWT_MESSAGE);
        } catch (Exception e) {
            LOGGER.log(Level.INFO, UNEXPECTED_ERROR_MESSAGE, e);
            throw new AccessDeniedException(UNEXPECTED_ERROR_MESSAGE);
        }
    }

    public ProfileEntity checkForExistedProfileProfileOrAccessDenied() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            Object principal = auth.getPrincipal();
            if (principal instanceof ProfileEntity) {
                ProfileEntity profileEntity = (ProfileEntity) auth.getPrincipal();
                return profileDAO.findProfileByEmail(profileEntity.getEmail());
            }
        }
        throw new AccessDeniedException(NON_PROFILE_EXCEPTION_MESSAGE);
    }
}
