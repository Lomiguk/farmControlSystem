package ru.skibin.farmsystem.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skibin.farmsystem.api.dto.JwtAuthenticationResponse;
import ru.skibin.farmsystem.api.dto.ProfileResponse;
import ru.skibin.farmsystem.api.enumTypes.JwtType;
import ru.skibin.farmsystem.api.request.security.SignInRequest;
import ru.skibin.farmsystem.api.request.security.SignUpRequest;
import ru.skibin.farmsystem.entity.ProfileEntity;
import ru.skibin.farmsystem.repository.ProfileDAO;
import ru.skibin.farmsystem.service.ProfileService;
import ru.skibin.farmsystem.service.validation.AuthorizationCheckHelper;
import ru.skibin.farmsystem.util.PasswordUtil;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final ProfileDAO profileDAO;
    private final ProfileService profileService;
    private final JwtService jwtService;
    private final AuthorizationCheckHelper checkHelper;
    private final AuthenticationManager authenticationManager;

    /**
     * Profile sign-up
     * @param request user data
     * @return token
     */
    @Transactional
    public ProfileResponse signUp(SignUpRequest request) {
        String encodedPassword = new BCryptPasswordEncoder()
                .encode(PasswordUtil.getHash(request.getPassword()).toString());
        ProfileEntity profileEntity = ProfileEntity.builder()
                .fio(request.getFio())
                .email(request.getEmail())
                .password(encodedPassword)
                .role(request.getRole())
                .build();

        return profileService.save(profileEntity);
    }

    /**
     * Profile authentication
     * @param request user data
     * @return token
     */
    @Transactional
    public JwtAuthenticationResponse signIn(SignInRequest request) {
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
     * @return authorized profile id
     */
    public Long getProfileId() {
        ProfileEntity authProfile = checkHelper.checkForExistedAuthorizedProfile();

        return authProfile == null ? null : authProfile.getId();
    }

    /**
     * Invalidate authorized profile token
     * @return invalidation status
     */
    @Transactional
    public Boolean invalidateToken() {
        Long profileId = getProfileId();

        return profileId != null && jwtService.deleteProfileToken(profileId);
    }
}
