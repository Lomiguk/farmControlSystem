package ru.skibin.farmsystem.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@Schema(description = "Response with access token")
public class JwtAuthenticationResponse {
    @Schema(
            description = "Access token",
            example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJPbmxpbmUgSldUIEJ1aW..."
    )
    private final String accessToken;
    @Schema(
            description = "Refresh token",
            example = "KV1QiLCJhbGciiJPbmxpbmOiJIUzI1eyJ0eXAiOiJNiJ9.eyJpc3MiOUgSldUIEJ1aW..."
    )
    private final String refreshToken;
}
