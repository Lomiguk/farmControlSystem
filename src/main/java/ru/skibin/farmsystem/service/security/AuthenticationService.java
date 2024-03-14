package ru.skibin.farmsystem.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.skibin.farmsystem.api.data.enumTypes.Role;
import ru.skibin.farmsystem.api.response.JwtAuthenticationResponse;
import ru.skibin.farmsystem.api.response.ProfileResponse;
import ru.skibin.farmsystem.api.data.enumTypes.JwtType;
import ru.skibin.farmsystem.api.request.security.SignInRequest;
import ru.skibin.farmsystem.api.request.security.SignUpRequest;
import ru.skibin.farmsystem.entity.ProfileEntity;
import ru.skibin.farmsystem.repository.ProfileDAO;
import ru.skibin.farmsystem.service.ProfileService;
import ru.skibin.farmsystem.service.validation.AuthorizationCheckHelper;
import ru.skibin.farmsystem.service.validation.CommonCheckHelper;
import ru.skibin.farmsystem.util.PasswordUtil;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final ProfileDAO profileDAO;
    private final ProfileService profileService;
    private final JwtService jwtService;
    private final AuthorizationCheckHelper authorizationCheckHelper;
    private final CommonCheckHelper checkHelper;
    private final AuthenticationManager authenticationManager;

    /**
     * Profile sign-up
     *
     * @param request user data
     * @return token
     */
    @Transactional
    public ProfileResponse signUp(BindingResult bindingResult, SignUpRequest request) {
        checkHelper.chainCheckValidation(bindingResult);
        String encodedPassword = new BCryptPasswordEncoder()
                .encode(PasswordUtil.getHash(request.getPassword()).toString());
        ProfileEntity profileEntity = ProfileEntity.builder()
                .fio(request.getFio())
                .email(request.getEmail())
                .password(encodedPassword)
                .role(Role.USER)
                .build();

        return profileService.add(profileEntity);
    }

    /**
     * Profile authentication
     *
     * @param request user data
     * @return token
     */
    @Transactional
    public JwtAuthenticationResponse signIn(BindingResult bindingResult, SignInRequest request) {
        checkHelper.chainCheckValidation(bindingResult);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                PasswordUtil.getHash(request.getPassword()).toString()
        ));

        var userDetails = profileService
                .userDetailsService()
                .loadUserByUsername(request.getEmail());
        ProfileEntity profileEntity = profileDAO.findProfileByEmail(request.getEmail());
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        String accessJwt = jwtService.registerToken(profileEntity.getId(), accessToken, JwtType.ACCESS);
        String refreshJwt = jwtService.registerToken(profileEntity.getId(), refreshToken, JwtType.REFRESH);

        return new JwtAuthenticationResponse(accessJwt, refreshJwt);
    }

    /**
     * Getting authorized profile id
     *
     * @return authorized profile id
     */
    public Long getProfileId() {
        ProfileEntity authProfile = authorizationCheckHelper.checkForExistedAuthorizedProfileFromContext();

        return authProfile == null ? null : authProfile.getId();
    }

    /**
     * Invalidate authorized profile token
     *
     * @return invalidation status
     */
    @Transactional
    public Boolean invalidateToken() {
        Long profileId = getProfileId();

        return profileId != null && jwtService.deleteProfileToken(profileId);
    }

    /**
     * Refreshing profile's tokens
     *
     * @param refreshToken Refresh token
     * @return new access & refresh tokens
     */
    @Transactional
    public JwtAuthenticationResponse refreshTokens(String refreshToken) {
        jwtService.validateRefreshToken(refreshToken);
        String userLogin = jwtService.extractRefreshUserLogin(refreshToken);
        UserDetails userDetails = profileService
                .userDetailsService()
                .loadUserByUsername(userLogin);

        ProfileEntity profile = profileDAO.findProfileByEmail(userLogin);
        checkHelper.checkProfileForExistByEmail(userLogin);
        jwtService.deleteProfileToken(profile.getId());
        String newAccessToken = jwtService.generateAccessToken(userDetails);
        String newRefreshToken = jwtService.generateRefreshToken(userDetails);
        jwtService.registerToken(profile.getId(), newAccessToken, JwtType.ACCESS);
        jwtService.registerToken(profile.getId(), newRefreshToken, JwtType.REFRESH);

        return new JwtAuthenticationResponse(newAccessToken, newRefreshToken);
    }
}
