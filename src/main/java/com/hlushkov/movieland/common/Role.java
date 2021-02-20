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

}
