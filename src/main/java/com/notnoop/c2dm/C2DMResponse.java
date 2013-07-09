package com.notnoop.c2dm;

/**
 * Date: 09.07.13
 * Time: 15:33
 */
public class C2DMResponse {

    private C2DMResponseStatus status;
    private String messageId = "";

    public C2DMResponse(C2DMResponseStatus status, String messageId) {
        this.status = status;
        this.messageId = messageId;
    }

    public C2DMResponse(C2DMResponseStatus status) {
        this.status = status;
    }

    public C2DMResponseStatus getStatus() {
        return status;
    }

    public String getMessageId() {
        return messageId;
    }
}
