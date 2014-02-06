package com.notnoop.c2dm;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Date: 09.07.13
 * Time: 15:33
 */
public class GCMResponse {

    private static final List<Map<String, ?>> EMPTY = Collections.unmodifiableList(new ArrayList<Map<String, ?>>(1));

    @SerializedName("multicast_id")
    private long multicastId;
    private int success;
    private int failure;

    @SerializedName("canonical_ids")
    private int canonicalIds;
    private List<Map<String, ?>> results;

    private GCMResponseStatus status = GCMResponseStatus.SUCCESSFUL;

    public GCMResponse(long multicastId, int success, int failure, int canonicalIds,
                       List<Map<String, ?>> results, GCMResponseStatus status) {
        this.multicastId = multicastId;
        this.success = success;
        this.failure = failure;
        this.canonicalIds = canonicalIds;
        this.results = results;
        this.status = status;
    }

    public static GCMResponse withStatus(GCMResponseStatus status) {
        return new GCMResponse(0, 0, 1, 0, EMPTY, status);
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

    public List<Map<String, ?>> getResults() {
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
