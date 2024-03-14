package ru.skibin.farmsystem.util;

import java.util.List;

public class SecurityConfigurationConstants {
    public static final String POST_ADD_PROFILE = "/profile";
    public static final String DELETE_DEL_PROFILE = "/profile/*";
    public static final String PUT_UPDATE_PROFILE = "/profile/*";
    public static final String PATCH_FIO_PROFILE = "/profile/*/info";
    public static final String PATCH_ROLE_PROFILE = "/profile/*/admin";
    public static final String PATCH_ACTIVE_PROFILE = "/profile/*/active";
    public static final String PATCH_PASSWORD_PROFILE = "/profile/*/password";
    public static final String PUT_UPDATE_PRODUCT = "/product/*";
    public static final String DELETE_DEL_PRODUCT = "/product/*";
    public static final String POST_ADD_PRODUCT = "/product";
    public static final String PATCH_PRODUCT = "/product/**";
    public static final String PATCH_ACTION = "/action/**";
    public static final String POST_MARK = "/profile/mark";
    public static final String GET_MARK = "/profile/mark/*";
    public static final String GET_PROFILE_MARK = "/profile/*/mark";
    public static final String GET_MARK_BY_DAY = "/profile/mark/day";
    public static final String POST_GET_MARK_BY_PERIOD = "/profile/mark/period";
    public static final String PUT_UPDATE_MARK = "/profile/mark/*";
    public static final String DELETE_MARK = "/profile/mark/*";
    public static final String STATISTIC = "/statistic/**";
    public static final String SWAGGER_UI = "/swagger-ui/**";
    public static final String SWAGGER_RESOURCES = "/swagger-resources/*";
    public static final String SWAGGER_DOC_API = "/v3/api-docs/**";
    public static final List<String> HTTP_METHODS = List.of("GET", "POST", "PUT", "PATCH", "DELETE");
    public static final List<String> ORIGINS_PATTERNS = List.of("*");
    public static final List<String> HEADERS = List.of("*");
    public static final Boolean ALLOW_CREDENTIALS = true;
}
