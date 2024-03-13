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
import ru.skibin.farmsystem.api.data.enumTypes.Role;
import ru.skibin.farmsystem.security.filter.JwtFilter;
import ru.skibin.farmsystem.service.ProfileService;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SpringSecurityConfiguration {
    private final String POST_ADD_PROFILE = "/profile";
    private final String DELETE_DEL_PROFILE = "/profile/*";
    private final String PUT_UPDATE_PROFILE = "/profile/*";
    private final String PATCH_FIO_PROFILE = "/profile/*/info";
    private final String PATCH_ROLE_PROFILE = "/profile/*/admin";
    private final String PATCH_ACTIVE_PROFILE = "/profile/*/active";
    private final String PATCH_PASSWORD_PROFILE = "/profile/*/password";
    private final String PUT_UPDATE_PRODUCT = "/product/*";
    private final String DELETE_DEL_PRODUCT = "/product/*";
    private final String POST_ADD_PRODUCT = "/product";
    private final String PATCH_PRODUCT = "/product/**";
    private final String PATCH_ACTION = "/action/**";
    private final String STATISTIC = "/statistic/**";
    private final String AUTH = "/auth/**";
    private final String SWAGGER_UI = "/swagger-ui/**";
    private final String SWAGGER_RESOURCES = "/swagger-resources/*";
    private final String SWAGGER_DOC_API = "/v3/api-docs/**";

    private final JwtFilter jwtFilter;
    private final ProfileService profileService;
    private final String ADMIN_ROLE = Role.ADMIN.name();

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                // CORS disable (allowing requests from all domains)
                .cors(cors -> cors.configurationSource(
                        request -> {
                            var corsConfiguration = new CorsConfiguration();
                            corsConfiguration.setAllowedOriginPatterns(List.of("*"));
                            corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE"));
                            corsConfiguration.setAllowedHeaders(List.of("*"));
                            corsConfiguration.setAllowCredentials(true);
                            return corsConfiguration;
                        }
                ))
                // set setting for endpoints access
                .authorizeHttpRequests(request -> request
                        .requestMatchers(AUTH).permitAll()
                        .requestMatchers(SWAGGER_UI, SWAGGER_RESOURCES, SWAGGER_DOC_API).permitAll()
                        .requestMatchers(HttpMethod.POST, POST_ADD_PROFILE).hasAuthority(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.DELETE, DELETE_DEL_PROFILE).hasAuthority(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.PUT, PUT_UPDATE_PROFILE).hasAuthority(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.PATCH, PATCH_FIO_PROFILE).hasAuthority(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.PATCH, PATCH_ROLE_PROFILE).hasAuthority(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.PATCH, PATCH_ACTIVE_PROFILE).hasAuthority(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.PUT, PUT_UPDATE_PRODUCT).hasAuthority(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.DELETE, DELETE_DEL_PRODUCT).hasAuthority(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.POST, POST_ADD_PRODUCT).hasAuthority(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.PATCH, PATCH_PRODUCT).hasAuthority(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.PATCH, PATCH_PASSWORD_PROFILE).authenticated()
                        .requestMatchers(HttpMethod.PATCH, PATCH_ACTION).authenticated()
                        .requestMatchers(STATISTIC).hasRole(ADMIN_ROLE)
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
