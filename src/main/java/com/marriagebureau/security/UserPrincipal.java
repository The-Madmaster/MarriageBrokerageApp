package com.marriagebureau.security;

import com.marriagebureau.usermanagement.entity.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

    private final Long userId;
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean enabled;

    public UserPrincipal(AppUser appUser) {
        this.userId = appUser.getId(); // Lombok generated getId()
        this.username = appUser.getUsername(); // Lombok generated getUsername()
        this.password = appUser.getPassword(); // Lombok generated getPassword()
        this.enabled = appUser.isEnabled(); // Lombok generated isEnabled() for boolean field 'enabled'
        this.authorities = appUser.getRoles().stream() // Lombok generated getRoles()
                                  .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                                  .collect(Collectors.toList());
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}