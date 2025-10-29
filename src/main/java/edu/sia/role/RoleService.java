package edu.sia.role;

import org.springframework.stereotype.Service;

@Service
public class RoleService {

    public boolean hasPermission(String roleName, String permissionName) {
        Role role;
        Permission permission;
        try {
            role = Role.fromValue(roleName); // Convierte el nombre del rol a un objeto Role
            permission = Permission.valueOf(permissionName); // Convierte el nombre del permiso a un objeto Permission
        } catch (IllegalArgumentException e) {
            return false; // Rol o permiso no válido
        }
        return role.getPermissions().contains(permission); // Verifica si el rol tiene el permiso
    }
}
