package com.notnoop.c2dm;

import com.google.gson.internal.StringMap;

/**
 * Date: 09.07.13
 * Time: 15:33
 */
public class GCMResponse {

    private long multicastId;
    private int success;
    private int failure;
    private int canonicalIds;
    private StringMap<String> results;
    private GCMResponseStatus status;

    public GCMResponse(long multicastId, int success, int failure, int canonicalIds,
                       StringMap<String> results, GCMResponseStatus status) {
        this.multicastId = multicastId;
        this.success = success;
        this.failure = failure;
        this.canonicalIds = canonicalIds;
        this.results = results;
        this.status = status;
    }

    public long getMulticastId() {
        return multicastId;
    }

    public int getSuccess() {
        return success;
    }

    public int getFailure() {
        return failure;
    }

    public int getCanonicalIds() {
        return canonicalIds;
    }

    public StringMap<String> getResults() {
        return results;
    }

    public GCMResponseStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "GCMResponse{" +
                "multicastId=" + multicastId +
                ", success=" + success +
                ", failure=" + failure +
                ", canonicalIds=" + canonicalIds +
                ", results=" + results +
                ", status=" + status +
                '}';
    }
}
