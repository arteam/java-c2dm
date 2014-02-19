package com.notnoop.c2dm.domain;

/**
 * Date: 06.02.14
 * Time: 20:07
 *
 * @author Artem Prigoda
 */
public enum GCMStatus {

    SUCCESSFUL, SERVER_UNAVAILABLE, INVALID_REQUEST, INVALID_AUTHENTICATION, UNKNOWN_ERROR;

    public static GCMStatus valueOf(int code) {
        switch (code) {
            case 200:
                return SUCCESSFUL;
            case 503:
                return SERVER_UNAVAILABLE;
            case 400:
                return INVALID_REQUEST;
            case 401:
                return INVALID_AUTHENTICATION;
            default:
                return UNKNOWN_ERROR;
        }
    }

}
