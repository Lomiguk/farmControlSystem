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
import ru.skibin.farmsystem.api.enumTypes.Role;
import ru.skibin.farmsystem.security.filter.JwtFilter;
import ru.skibin.farmsystem.service.ProfileService;

import java.util.List;

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
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/swagger-resources/*", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/profile").hasAuthority(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.DELETE, "/profile/*").hasAuthority(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.PUT, "/profile/*").hasAuthority(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.PATCH, "/profile/*/info").hasAuthority(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.PATCH, "/profile/*/admin").hasAuthority(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.PATCH, "/profile/*/active").hasAuthority(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.PATCH, "/profile/*/password").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/product/*").hasAuthority(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.DELETE, "/product/*").hasAuthority(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.POST, "/product").hasAuthority(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.PATCH, "/product/**").hasAuthority(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.PATCH, "/action/**").authenticated()
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
