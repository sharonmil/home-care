package com.silvana.homecare.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ROLE_PACIENTE = "ROLE_PACIENTE";

    public static final String ROLE_FAMILIAR = "ROLE_FAMILIAR";

    public static final String ROLE_MEDICO = "ROLE_MEDICO";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {}
}
