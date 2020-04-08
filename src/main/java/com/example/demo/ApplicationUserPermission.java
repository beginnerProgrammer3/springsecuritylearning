package com.example.demo;

public enum ApplicationUserPermission {
    GIRL_READ("girl:read"),
    GIRL_WRITE("girl:write");


    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
