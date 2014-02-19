/*
 * Copyright 2011, Mahmood Ali.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above
 *     copyright notice, this list of conditions and the following disclaimer
 *     in the documentation and/or other materials provided with the
 *     distribution.
 *   * Neither the name of Mahmood Ali. nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.notnoop.c2dm.domain;

/**
 * Represents the logical response of GCMService
 */
public enum GCMError {

    /**
     * Too many messages sent by the sender. Retry after a while.
     */
    UNAVAILABLE("Unavailable", true),

    /**
     * Missing or bad registration_id. Sender should stop sending messages to
     * this device.
     */
    INVALID_REGISTRATION("InvalidRegistration", false),

    /**
     * The registration_id is no longer valid, for example user has
     * uninstalled the application or turned off notifications. Sender should
     * stop sending messages to this device.
     */
    NOT_REGISTERED("NotRegistered", false);


    private final String key;
    private final boolean shouldRetry;

    public static final GCMError[] logicalResponses = GCMError.values();

    GCMError(String key, boolean shouldRetry) {
        this.key = key;
        this.shouldRetry = shouldRetry;
    }

    public boolean shouldRetry() {
        return shouldRetry;
    }

    public String getKey() {
        return key;
    }

    public static GCMError of(String key) {
        for (GCMError r : GCMError.logicalResponses) {
            if (key.equals(r.getKey())) {
                return r;
            }
        }
        return null;
    }
}
