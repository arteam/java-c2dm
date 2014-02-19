package com.notnoop.c2dm.domain;

import com.google.gson.annotations.SerializedName;

/**
 * Date: 06.02.14
 * Time: 19:59
 *
 * @author Artem Prigoda
 */
public class CanonicalId {

    @SerializedName("message_id")
    private String messageId;

    @SerializedName("registration_id")
    private String registrationId;

    @SerializedName("new_registration_id")
    private String newRegistrationId;

    public CanonicalId(String messageId, String registrationId, String newRegistrationId) {
        this.messageId = messageId;
        this.registrationId = registrationId;
        this.newRegistrationId = newRegistrationId;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public String getNewRegistrationId() {
        return newRegistrationId;
    }

    @Override
    public String toString() {
        return "CanonicalId{" +
                "messageId='" + messageId + '\'' +
                ", registrationId='" + registrationId + '\'' +
                ", newRegistrationId='" + newRegistrationId + '\'' +
                '}';
    }
}
