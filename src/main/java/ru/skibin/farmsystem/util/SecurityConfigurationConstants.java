package ru.skibin.farmsystem.util;

import ru.skibin.farmsystem.api.data.enumTypes.Role;

import java.util.List;

public class SecurityConfigurationConstants {
    public static final String POST_ADD_PROFILE = "/profile";
    public static final String POST_TASK = "/task/**";
    public static final String DELETE_DEL_PROFILE = "/profile/*";
    public static final String PUT_UPDATE_PROFILE = "/profile/*";
    public static final String PATCH_FIO_PROFILE = "/profile/*/info";
    public static final String PATCH_ROLE_PROFILE = "/profile/*/admin";
    public static final String PATCH_ACTIVE_PROFILE = "/profile/*/active";
    public static final String PATCH_PASSWORD_PROFILE = "/profile/*/password";
    public static final String PATCH_TASK_COLLECTED_VALUE = "/task/*";
    public static final String PUT_UPDATE_PRODUCT = "/product/*";
    public static final String PUT_TASK = "/task/**";
    public static final String DELETE_DEL_PRODUCT = "/product/*";
    public static final String DELETE_TASK = "/task/**";
    public static final String POST_ADD_PRODUCT = "/product";
    public static final String PATCH_PRODUCT = "/product/**";
    public static final String PATCH_ACTION = "/action/**";
    public static final String POST_MARK = "/profile/mark";
    public static final String GET_MARK = "/profile/mark/*";
    public static final String GET_PROFILE_MARK = "/profile/*/mark";
    public static final String GET_MARK_BY_DAY = "/profile/mark/day";
    public static final String GET_PROFILE_TASK = "/task/profile/*";
    public static final String GET_THE_TASK = "/task/*";
    public static final String POST_GET_MARK_BY_PERIOD = "/profile/mark/period";
    public static final String PUT_UPDATE_MARK = "/profile/mark/*";
    public static final String DELETE_MARK = "/profile/mark/*";
    public static final String STATISTIC = "/statistic/**";
    public static final String AUTH = "/auth/**";
    public static final String SWAGGER_UI = "/swagger-ui/**";
    public static final String SWAGGER_RESOURCES = "/swagger-resources/*";
    public static final String SWAGGER_DOC_API = "/v3/api-docs/**";
    public static final List<String> HTTP_METHODS = List.of("GET", "POST", "PUT", "PATCH", "DELETE");
    public static final List<String> ORIGINS_PATTERNS = List.of("*");
    public static final List<String> HEADERS = List.of("*");
    public static final Boolean ALLOW_CREDENTIALS = true;
    public static final String ADMIN_ROLE = Role.ADMIN.name();

    public static final String[] ADMIN_ROOTS = {
            STATISTIC
    };
    public static final String[] ADMIN_HTTP_POST = {
            POST_ADD_PROFILE,
            POST_ADD_PRODUCT,
            POST_MARK,
            POST_GET_MARK_BY_PERIOD,
            POST_TASK,
            DELETE_TASK,
            PUT_TASK
    };
    public static final String[] ADMIN_HTTP_DELETE = {
            DELETE_DEL_PROFILE,
            DELETE_DEL_PRODUCT,
            DELETE_MARK
    };
    public static final String[] ADMIN_HTTP_PUT = {
            PUT_UPDATE_PROFILE,
            PUT_UPDATE_PRODUCT,
            PUT_UPDATE_MARK
    };
    public static final String[] ADMIN_HTTP_PATCH = {
            PATCH_FIO_PROFILE,
            PATCH_ROLE_PROFILE,
            PATCH_ACTIVE_PROFILE,
            PATCH_PRODUCT,
            PATCH_TASK_COLLECTED_VALUE
    };
    public static final String[] ADMIN_HTTP_GET = {
            GET_MARK_BY_DAY,
            GET_THE_TASK
    };
    public static final String[] AUTHENTICATED_HTTP_PATCH = {
            PATCH_PASSWORD_PROFILE,
            PATCH_ACTION
    };
    public static final String[] AUTHENTICATED_HTTP_GET = {
            GET_MARK,
            GET_PROFILE_MARK,
            GET_PROFILE_TASK
    };
    public static final String[] PERMITTED_ALL = {
            AUTH,
            SWAGGER_UI,
            SWAGGER_RESOURCES,
            SWAGGER_DOC_API
    };
}
