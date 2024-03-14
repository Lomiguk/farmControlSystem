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

import static org.apache.naming.ResourceRef.AUTH;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.ALLOW_CREDENTIALS;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.DELETE_DEL_PRODUCT;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.DELETE_DEL_PROFILE;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.DELETE_MARK;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.GET_MARK;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.GET_MARK_BY_DAY;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.GET_PROFILE_MARK;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.HEADERS;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.HTTP_METHODS;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.ORIGINS_PATTERNS;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.PATCH_ACTION;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.PATCH_ACTIVE_PROFILE;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.PATCH_FIO_PROFILE;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.PATCH_PASSWORD_PROFILE;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.PATCH_PRODUCT;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.PATCH_ROLE_PROFILE;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.POST_ADD_PRODUCT;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.POST_ADD_PROFILE;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.POST_GET_MARK_BY_PERIOD;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.POST_MARK;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.PUT_UPDATE_MARK;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.PUT_UPDATE_PRODUCT;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.PUT_UPDATE_PROFILE;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.STATISTIC;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.SWAGGER_DOC_API;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.SWAGGER_RESOURCES;
import static ru.skibin.farmsystem.util.SecurityConfigurationConstants.SWAGGER_UI;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SpringSecurityConfiguration {
    private final JwtFilter jwtFilter;
    private final ProfileService profileService;
    private final String ADMIN_ROLE = Role.ADMIN.name();

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(
                        request -> {
                            var corsConfiguration = new CorsConfiguration();
                            corsConfiguration.setAllowedOriginPatterns(ORIGINS_PATTERNS);
                            corsConfiguration.setAllowedMethods(HTTP_METHODS);
                            corsConfiguration.setAllowedHeaders(HEADERS);
                            corsConfiguration.setAllowCredentials(ALLOW_CREDENTIALS);
                            return corsConfiguration;
                        }
                ))
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
                        .requestMatchers(STATISTIC).hasAuthority(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.POST, POST_MARK).hasAuthority(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.GET, GET_MARK_BY_DAY).hasAuthority(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.POST, POST_GET_MARK_BY_PERIOD).hasAuthority(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.PUT, PUT_UPDATE_MARK).hasAuthority(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.DELETE, DELETE_MARK).hasAuthority(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.GET, GET_MARK, GET_PROFILE_MARK).authenticated()
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
