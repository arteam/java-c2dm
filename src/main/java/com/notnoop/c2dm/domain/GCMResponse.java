package com.notnoop.c2dm.domain;

import java.util.List;
import java.util.Map;

/**
 * Date: 09.07.13
 * Time: 15:33
 */
public class GCMResponse {

    /**
     * Id of multicast
     */
    private long multicastId;

    /**
     * Amount of successful messages
     */
    private int successLength;

    /**
     * Amount of failures
     */
    private int failureLength;

    /**
     * Map of errors
     */
    private Map<GCMError, List<String>> failures;

    /**
     * List of canonical ids (Devices that change registration id)
     */
    private List<CanonicalId> canonicalIds;

    public GCMResponse(long multicastId, int successLength, int failureLength, Map<GCMError, List<String>> failures,
                       List<CanonicalId> canonicalIds) {
        this.multicastId = multicastId;
        this.successLength = successLength;
        this.failureLength = failureLength;
        this.failures = failures;
        this.canonicalIds = canonicalIds;
    }

    public long getMulticastId() {
        return multicastId;
    }

    public int getSuccessLength() {
        return successLength;
    }

    public Map<GCMError, List<String>> getFailures() {
        return failures;
    }

    public List<CanonicalId> getCanonicalIds() {
        return canonicalIds;
    }

    public int getFailureLength() {
        return failureLength;
    }

    @Override
    public String toString() {
        return "GCMResponse{" +
                "multicastId=" + multicastId +
                ", successLength=" + successLength +
                ", failureLength=" + failureLength +
                ", failures=" + failures +
                ", canonicalIds=" + canonicalIds +
                '}';
    }
}
