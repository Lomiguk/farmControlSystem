package ru.skibin.farmsystem.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skibin.farmsystem.api.dto.JwtAuthenticationResponse;
import ru.skibin.farmsystem.api.request.security.SignInRequest;
import ru.skibin.farmsystem.api.request.security.SignUpRequest;
import ru.skibin.farmsystem.entity.ProfileEntity;
import ru.skibin.farmsystem.service.ProfileService;
import ru.skibin.farmsystem.util.PasswordUtil;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final ProfileService profileService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Profile sign-up
     * @param request user data
     * @return token
     */
    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        ProfileEntity profileEntity = ProfileEntity.builder()
                .fio(request.getFio())
                .email(request.getEmail())
                .password(PasswordUtil.getHash(request.getPassword()).toString())
                .isAdmin(request.getIsAdmin())
                .build();

        ProfileEntity savedProfileEntity = profileService.save(profileEntity);

        var jwt = jwtService.generateToken(savedProfileEntity);
        return new JwtAuthenticationResponse(jwt);
    }

    /**
     * Profile authentication
     * @param request user data
     * @return token
     */
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));

        var user = profileService
                .userDetailsService()
                .loadUserByUsername(request.getEmail());

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }
}
