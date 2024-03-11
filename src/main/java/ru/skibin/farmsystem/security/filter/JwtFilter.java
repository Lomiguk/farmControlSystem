package ru.skibin.farmsystem.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.skibin.farmsystem.security.service.JwtService;
import ru.skibin.farmsystem.service.ProfileService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";
    private final JwtService jwtService;
    private final ProfileService profileService;


    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        // Getting token from header
        var authHeader = request.getHeader(HEADER_NAME);
        if (authHeader.isBlank() || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        // remove prefix for getting profile login from the token
        var jwt = authHeader.substring(BEARER_PREFIX.length());
        var userLogin = jwtService.extractUserLogin(jwt);

        if (userLogin.isBlank() && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = profileService
                    .userDetailsService()
                    .loadUserByUsername(userLogin);
            // validate user if token is valid
            if (jwtService.isTokenValid(jwt, userDetails)) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
        }
        filterChain.doFilter(request, response);
    }

    /*@Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {
        String jwt = getJwtTokenFromRequest(request);
        var userLogin = jwtService.extractUserLogin(jwt);
        UserDetails userDetails = profileService
                .userDetailsService()
                .loadUserByUsername(userLogin);
        if (jwt != null && jwtService.isTokenValid(jwt, userDetails)) {
            setSecurityContextHolderAuthentication(request, jwt, userDetails);
        }
        chain.doFilter(request, response);
    }

    private String getJwtTokenFromRequest(HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization");
        if (StringUtils.hasText(jwtToken) && jwtToken.startsWith("Bearer ")) {
            return jwtToken.substring(7);
        }
        return null;
    }

    private void setSecurityContextHolderAuthentication(HttpServletRequest request, String jwtToken, UserDetails userDetails) {
        String username = jwtService.extractUserLogin(jwtToken);

        if (username != null && jwtService.isTokenValid(jwtToken, userDetails) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(token);
        }
    }*/
}
