package ru.skibin.farmsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileEntity implements UserDetails {
    Long id;
    String fio;
    String email;
    String password;
    Boolean isAdmin;
    Boolean isActual;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = isAdmin ? "ADMIN" : "USER";

        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActual;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActual;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActual;
    }

    @Override
    public boolean isEnabled() {
        return isActual;
    }
}
