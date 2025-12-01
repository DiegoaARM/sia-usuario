package edu.sia.security;

import edu.sia.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Reactive Permission Evaluator for WebFlux.
 * Note: WebFlux security method expressions are evaluated reactively,
 * but this class maintains compatibility with standard PermissionEvaluator interface.
 */
@Component
public class ReactivePermissionEvaluator implements PermissionEvaluator {

    private final RoleService roleService;

    @Autowired
    public ReactivePermissionEvaluator(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || permission == null) {
            return false;
        }

        String roleName = authentication.getAuthorities()
                .stream()
                .findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .orElse(null);

        if (roleName == null) {
            return false;
        }

        return roleService.hasPermission(roleName, permission.toString());
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return hasPermission(authentication, targetId, permission);
    }
}

