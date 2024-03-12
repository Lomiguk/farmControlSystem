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
import ru.skibin.farmsystem.api.dto.JwtAuthenticationResponse;
import ru.skibin.farmsystem.api.dto.ProfileResponse;
import ru.skibin.farmsystem.api.request.security.SignInRequest;
import ru.skibin.farmsystem.api.request.security.SignUpRequest;
import ru.skibin.farmsystem.exception.common.ValidationException;
import ru.skibin.farmsystem.service.security.AuthenticationService;
import ru.skibin.farmsystem.util.BindingResultUtil;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "User sign-up")
    @PostMapping("/sign-up")
    public ResponseEntity<ProfileResponse> signUp(
            @RequestBody @Valid SignUpRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) throw new ValidationException(
                BindingResultUtil.requestValidationToString(bindingResult)
        );
        return new ResponseEntity<>(
                authenticationService.signUp(request),
                HttpStatus.OK
        );
    }

    @Operation(summary = "User logout")
    @DeleteMapping("/logout")
    public ResponseEntity<Boolean> deleteToken() {
                return new ResponseEntity<>(
                authenticationService.invalidateToken(),
                HttpStatus.OK
        );
    }

    @Operation(summary = "User sign-in")
    @PostMapping("/sign-in")
    public ResponseEntity<JwtAuthenticationResponse> signIn(
            @RequestBody @Valid SignInRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) throw new ValidationException(
                BindingResultUtil.requestValidationToString(bindingResult)
        );
        return new ResponseEntity<>(
                authenticationService.signIn(request),
                HttpStatus.OK
        );
    }
}
