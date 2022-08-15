package com.practice.springbasic.config.jwt;

public interface JwtProperties {
    static final String SECRET = "@middleFittingStudyWebProject*@hoinfnopOOfno@eo@mpmf:mwm@qrfp@p@)!y!&:;NASFN";
    static final String TOKEN_PREFIX = "MiddleFittingBearer ";
    static final String ACCESS_HEADER_STRING = "Authorization";
    static final String REFRESH_HEADER_STRING = "Refresh";
    static final int ACCESS_EXPIRATION_TIME = 60000*60;
    static final int REFRESH_EXPIRATION_TIME = 60000*60*24;
}
