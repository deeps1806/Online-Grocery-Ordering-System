package com.ogos.util;

public final class Constants {

    private Constants() {}

    // JWT
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTH_HEADER   = "Authorization";

    // Roles
    public static final String ROLE_CUSTOMER = "ROLE_CUSTOMER";
    public static final String ROLE_VENDOR   = "ROLE_VENDOR";
    public static final String ROLE_ADMIN    = "ROLE_ADMIN";

    // Messages
    public static final String REGISTRATION_SUCCESS = "Registration Successful";
    public static final String LOGIN_SUCCESS         = "Login Successful";
}
