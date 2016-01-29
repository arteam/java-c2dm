package com.notnoop.c2dm.domain;

/**
 * Date: 1/29/16
 * Time: 3:16 PM
 *
 * @author Artem Prigoda
 */
public enum GCMPriority {
    NORMAL,
    HIGH;

    public String id() {
        return name().toLowerCase();
    }
}
