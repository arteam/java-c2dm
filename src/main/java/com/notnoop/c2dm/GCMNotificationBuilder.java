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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.notnoop.c2dm.domain.GCMPriority;

import java.util.Collections;
import java.util.List;

/**
 * Represents a builder for constructing the notifications requests, as
 * specified by <a
 * href="http://code.google.com/android/c2dm/index.html#server">Android Cloud to
 * Device Messaging Framework documentation</a>:
 */
public class GCMNotificationBuilder {
    private List<String> registrationIds;
    private String collapseKey;
    private boolean delayWhileIdle;
    private JsonObject data = new JsonObject();
    private GCMPriority priority = GCMPriority.NORMAL;

    public GCMNotificationBuilder() {
    }

    public GCMNotificationBuilder registrationId(String registrationId) {
        this.registrationIds = Collections.singletonList(registrationId);
        return this;
    }

    public GCMNotificationBuilder registrationIds(List<String> registrationIds) {
        this.registrationIds = registrationIds;
        return this;
    }

    /**
     * Sets the collapse key for the notification.
     * <p/>
     * An arbitrary string that is used to collapse a group of like messages
     * when the device is offline, so that only the last message gets sent to
     * the client. This is intended to avoid sending too many messages to the
     * phone when it comes back online. Note that since there is no guarantee of
     * the order in which messages get sent, the "last" message may not actually
     * be the last message sent by the application server.
     * <p/>
     * The field is optional
     *
     * @return this
     */
    public GCMNotificationBuilder collapseKey(String collapseKey) {
        this.collapseKey = collapseKey;
        return this;
    }

    /**
     * Sets the delay while idle flag for the message.
     * <p/>
     * indicates that the message should not be sent immediately if the device
     * is idle. The server will wait for the device to become active, and then
     * only the last message for each collapse_key value will be sent.
     * <p/>
     * Default value is false.
     *
     * @return this
     */
    public GCMNotificationBuilder delayWhileIdle(boolean delayWhileIdle) {
        this.delayWhileIdle = delayWhileIdle;
        return this;
    }

    /**
     * Appends a character value by a key
     */
    public GCMNotificationBuilder key(String key, Character value) {
        data.addProperty(key, value);
        return this;
    }

    /**
     * Appends a character value by a key
     */
    public GCMNotificationBuilder key(String key, Boolean value) {
        data.addProperty(key, value);
        return this;
    }

    /**
     * Appends a character value by a key
     */
    public GCMNotificationBuilder key(String key, Number value) {
        data.addProperty(key, value);
        return this;
    }

    /**
     * Appends a character value by a key
     */
    public GCMNotificationBuilder key(String key, String value) {
        data.addProperty(key, value);
        return this;
    }

    /**
     * Appends a character value by a key
     */
    public GCMNotificationBuilder key(String property, JsonElement value) {
        data.add(property, value);
        return this;
    }

    /**
     * Sets the message priority
     *
     * @param priority the message priority
     * @return this
     */
    public GCMNotificationBuilder priority(GCMPriority priority) {
        this.priority = priority;
        return this;
    }

    private void checkInitialization() {
        if (registrationIds == null) {
            throw new IllegalStateException("Registration id is required and missing");
        }
    }

    /**
     * Returns a fully initialized notification object
     */
    public GCMNotification build() {
        checkInitialization();
        return new GCMNotificationImpl(registrationIds, collapseKey, delayWhileIdle, data, priority.id());
    }

    public GCMNotificationBuilder data(JsonObject dataMap) {
        this.data = dataMap;
        return this;
    }
}
