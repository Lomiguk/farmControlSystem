package ru.skibin.farmsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skibin.farmsystem.api.request.security.SignInRequest;
import ru.skibin.farmsystem.api.request.security.SignUpRequest;
import ru.skibin.farmsystem.api.response.JwtAuthenticationResponse;
import ru.skibin.farmsystem.api.response.ProfileResponse;
import ru.skibin.farmsystem.exception.common.ValidationException;
import ru.skibin.farmsystem.service.security.AuthenticationService;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

    private final AuthenticationService authenticationService;

    /**
     * Creating new profile
     *
     * @param request       request with new profile data
     * @param bindingResult request validation data
     * @return Http response with created profile response model
     */
    @Operation(summary = "User sign-up")
    @PostMapping("/sign-up")
    public ResponseEntity<ProfileResponse> signUp(
            @RequestBody @Valid SignUpRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        return new ResponseEntity<>(
                authenticationService.signUp(request),
                HttpStatus.OK
        );
    }

    /**
     * Logout authorized profile
     *
     * @return true - if success
     */
    @Operation(summary = "User logout")
    @DeleteMapping("/logout")
    public ResponseEntity<Boolean> deleteToken() {
        return new ResponseEntity<>(
                authenticationService.invalidateToken(),
                HttpStatus.OK
        );
    }

    /**
     * Sign-in profile
     *
     * @param request       Profile credential
     * @param bindingResult Request validation data
     * @return Http response with access & refresh token
     */
    @Operation(summary = "User sign-in")
    @PostMapping("/sign-in")
    public ResponseEntity<JwtAuthenticationResponse> signIn(
            @RequestBody @Valid SignInRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        return new ResponseEntity<>(
                authenticationService.signIn(request),
                HttpStatus.OK
        );
    }

    /**
     * Updating user access token
     *
     * @param refreshToken refresh token
     * @return Http response with new access & refresh token
     */
    @Operation(summary = "Updating user access token")
    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refreshToken(@RequestBody String refreshToken) {
        return new ResponseEntity<>(
                authenticationService.refreshTokens(refreshToken),
                HttpStatus.OK
        );
    }
}
