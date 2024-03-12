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
import ru.skibin.farmsystem.service.validation.CommonCheckHelper;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
    private final CommonCheckHelper checkHelper;
    /**
     * Extract user login
     * @param token token
     * @return profile login
     */
    public String extractUserLogin(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Checking token for validation
     * @param token       token
     * @param userDetails user data
     * @return true, if token is valid
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userLogin = extractUserLogin(token);
        boolean isTokenExisted = jwtDAO.findToken(token) != null;
        return userLogin.equals(userDetails.getUsername()) &&
                !isTokenExpired(token) &&
                isTokenExisted;
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


    /**
     * Check token for expiration
     *
     * @param token token
     * @return true, if token is expired
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Getting time of expiration
     *
     * @param token token
     * @return time og expiration
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Getting all data from token
     *
     * @param token token
     * @return data
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(getSigningKey()))
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }

    /**
     * Getting key for token signing
     *
     * @return key
     */
    private byte[] getSigningKey() {
        return  Decoders.BASE64.decode(jwtSigningKey);
    }

    /**
     * Deleting profile tokens from repository
     * @param profileId profile identifier
     * @return deleting status
     */
    public Boolean deleteProfileToken(Long profileId) {
        checkHelper.checkProfileForExist(profileId, "Try to remove non-existed profile tokens");
        return jwtDAO.deleteProfileTokens(profileId) > 0;
    }

    /**
     * Deleting token from repository
     * @param tokenId token identifier
     * @return deleting status
     */
    public Boolean deleteToken(Long tokenId) {
        return jwtDAO.deleteToken(tokenId) > 0;
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
                .signWith(Keys.hmacShaKeyFor(getSigningKey()))
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + refreshLifetime.toMillis());

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(Keys.hmacShaKeyFor(getSigningKey()))
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
}
