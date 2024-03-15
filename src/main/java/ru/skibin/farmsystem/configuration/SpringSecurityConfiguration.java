package ru.skibin.farmsystem.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import ru.skibin.farmsystem.security.filter.JwtFilter;
import ru.skibin.farmsystem.service.ProfileService;

import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.ADMIN_HTTP_DELETE;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.ADMIN_HTTP_GET;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.ADMIN_HTTP_PATCH;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.ADMIN_HTTP_POST;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.ADMIN_HTTP_PUT;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.ADMIN_ROLE;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.ADMIN_ROOTS;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.ALLOW_CREDENTIALS;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.AUTHENTICATED_HTTP_GET;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.AUTHENTICATED_HTTP_PATCH;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.HEADERS;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.HTTP_METHODS;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.ORIGINS_PATTERNS;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.PERMITTED_ALL;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SpringSecurityConfiguration {
    private final JwtFilter jwtFilter;
    private final ProfileService profileService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(
                        request -> {
                            var corsConfiguration = new CorsConfiguration();
                            corsConfiguration.setAllowedOriginPatterns(ORIGINS_PATTERNS);
                            corsConfiguration.setAllowCredentials(ALLOW_CREDENTIALS);
                            corsConfiguration.setAllowedMethods(HTTP_METHODS);
                            corsConfiguration.setAllowedHeaders(HEADERS);
                            return corsConfiguration;
                        }
                ))
                .authorizeHttpRequests(request -> request
                        .requestMatchers(PERMITTED_ALL).permitAll()
                        .requestMatchers(ADMIN_ROOTS).hasAuthority(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.POST, ADMIN_HTTP_POST).hasAuthority(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.DELETE, ADMIN_HTTP_DELETE).hasAuthority(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.PUT, ADMIN_HTTP_PUT).hasAuthority(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.PATCH, ADMIN_HTTP_PATCH).hasAuthority(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.GET, ADMIN_HTTP_GET).hasAuthority(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.PATCH, AUTHENTICATED_HTTP_PATCH).authenticated()
                        .requestMatchers(HttpMethod.GET, AUTHENTICATED_HTTP_GET).authenticated()
                        .anyRequest().authenticated()
                )
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(profileService.userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
