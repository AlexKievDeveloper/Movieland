package com.hlushkov.movieland.common;

public enum Role {

    ADMIN("admin"), USER("user");

    private final String userRole;

    Role(String userRole) {
        this.userRole = userRole;
    }

    public String getUserRole() {
        return userRole;
    }

    public static Role getRole(String name) {
        Role[] userRoles = Role.values();
        for (Role userRole : userRoles) {
            if (userRole.userRole.equals(name)) {
                return userRole;
            }
        }

        throw new IllegalArgumentException("No role for name ".concat(name));
    }

}
