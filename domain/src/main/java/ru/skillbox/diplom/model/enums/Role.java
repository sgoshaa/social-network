package ru.skillbox.diplom.model.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {

    ROLE_USER(Set.of(Permission.USER)),
    ROLE_MODERATOR(Set.of(Permission.USER, Permission.MODERATE)),
    ROLE_ADMIN(Set.of(Permission.USER, Permission.MODERATE, Permission.ADMINISTRATION));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return permissions.stream()
                .map(p -> new SimpleGrantedAuthority(p.getPermission()))
                .collect(Collectors.toSet());
    }
}
