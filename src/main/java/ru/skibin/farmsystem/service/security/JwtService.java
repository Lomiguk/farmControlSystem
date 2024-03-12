package ru.skibin.farmsystem.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.skibin.farmsystem.api.enumTypes.JwtType;
import ru.skibin.farmsystem.repository.JwtDAO;
import ru.skibin.farmsystem.service.validation.AuthorizationCheckHelper;
import ru.skibin.farmsystem.service.validation.CommonCheckHelper;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${token.signing.key}")
    private String jwtSigningKey;
    @Value("${token.access.lifetime}")
    private Duration accessLifetime;

    @Value("${token.refresh.secret}")
    private String refreshJwtSigningKey;

    @Value("${token.refresh.lifetime}")
    private Duration refreshLifetime;

    private final JwtDAO jwtDAO;
    private final CommonCheckHelper commonCheckHelper;
    private final AuthorizationCheckHelper authorizationCheckHelper;
    private final Logger logger = Logger.getLogger(JwtService.class.getName());
    /**
     * Extract user login
     * @param token token
     * @return profile login
     */
    public String extractUserLogin(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public String extractRefreshUserLogin(String refreshToken) {
        return extractClaimRefresh(refreshToken, Claims::getSubject);
    }

    /**
     * Getting data from the token
     * @param token           token
     * @param claimsResolvers function for extraction data
     * @param <T>             data type
     * @return data
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final var claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private <T> T extractClaimRefresh(String token, Function<Claims, T> claimsResolvers) {
        final var claims = extractAllClaimsRefresh(token);
        return claimsResolvers.apply(claims);
    }

    /**
     * Getting all data from token
     * @param token token
     * @return data
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigKey())
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }

    private Claims extractAllClaimsRefresh(String token) {
        return Jwts.parser()
                .setSigningKey(getRefreshSigKey())
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }

    /**
     * Check token for expiration
     * @param token token
     * @return true, if token is expired
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Getting time of expiration
     * @param token token
     * @return time og expiration
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Key getRefreshSigKey() {
        return getKey(refreshJwtSigningKey);
    }
    private Key getSigKey() {
        return getKey(jwtSigningKey);
    }
    private Key getKey(String key) {
        return Keys.hmacShaKeyFor(getByteKey(key));
    }
    private byte[] getByteKey(String key) {
        return  Decoders.BASE64.decode(key);
    }

    /**
     * Deleting profile tokens from repository
     * @param profileId profile identifier
     * @return deleting status
     */
    public Boolean deleteProfileToken(Long profileId) {
        commonCheckHelper.checkProfileForExist(profileId, "Try to remove non-existed profile tokens");
        return jwtDAO.deleteProfileTokens(profileId) > 0;
    }

    public String generateAccessToken(UserDetails userDetails) {
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + accessLifetime.toMillis());
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", roles);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(getSigKey())
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + refreshLifetime.toMillis());

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(getRefreshSigKey())
                .compact();
    }

    public String registerToken(Long profileId, String accessToken, JwtType jwtType) {
        jwtDAO.addJwtToken(
                profileId,
                accessToken,
                jwtType
        );
        return jwtDAO.findToken(accessToken).getToken();
    }

    public Boolean validateRefreshToken(String refreshToken) {
        return authorizationCheckHelper.boolCheckTokenForValidation(refreshToken, getRefreshSigKey());
    }

    public Boolean validateAccessToken(String token) {
        return authorizationCheckHelper.boolCheckTokenForValidation(token, getSigKey());
    }
}
