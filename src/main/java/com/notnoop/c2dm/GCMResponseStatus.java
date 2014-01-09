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
package com.notnoop.c2dm;

/**
 * Represents the logical response of GCMService
 */
public enum GCMResponseStatus {
    /**
     * Message was sent successfully
     */
    SUCCESSFUL(null, false),

    /**
     * Too many messages sent by the sender. Retry after a while.
     */
    QUOTA_EXCEEDED("QuotaExceeded", true),

    /**
     * Too many messages sent by the sender to a specific device. Retry after
     * a while.
     */
    DEVICE_QUOTA_EXCEEDED("DeviceQuotaExceeded", true),

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
    NOT_REGISTERED("NotRegistered", false),

    /**
     * The payload of the message is too big, see the limitations. Reduce the
     * size of the message.
     */
    MESSAGE_TO_BIG("MessageTooBig", false),

    /**
     * Collapse key is required. Include collapse key in the request.
     */
    MISSING_COLLAPSE_KEY("MissingCollapseKey", false),

    SERVER_UNAVAILABLE(null, true),

    INVALID_AUTHENTICATION(null, false),

    INVALID_REQUEST("Invalid request", false),

    UNKNOWN_ERROR(null, false);

    private final String key;
    private final boolean shouldRetry;

    public static final GCMResponseStatus[] logicalResponses = GCMResponseStatus.values();

    GCMResponseStatus(String key, boolean shouldRetry) {
        this.key = key;
        this.shouldRetry = shouldRetry;
    }

    public boolean shouldRetry() {
        return shouldRetry;
    }

    public String getKey() {
        return key;
    }

    public boolean isSuccessful() {
        return this == SUCCESSFUL;
    }

    public static GCMResponseStatus of(String key) {
        for (GCMResponseStatus r : GCMResponseStatus.logicalResponses) {
            if (key.equals(r.getKey())) {
                return r;
            }
        }
        return UNKNOWN_ERROR;
    }
}
