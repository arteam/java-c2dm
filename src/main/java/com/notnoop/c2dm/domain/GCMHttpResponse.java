package com.notnoop.c2dm.domain;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Date: 09.07.13
 * Time: 15:33
 * {
 * "multicast_id": 216,
 * "success": 3,
 * "failure": 3,
 * "canonical_ids": 1,
 * "results": [
 * { "message_id": "1:0408" },
 * { "error": "Unavailable" },
 * { "error": "InvalidRegistration" },
 * { "message_id": "1:1516" },
 * { "message_id": "1:2342", "registration_id": "32" },
 * { "error": "NotRegistered"}
 * ]
 * }
 */
public class GCMHttpResponse {

    @SerializedName("multicast_id")
    private long multicastId;

    private int success;

    private int failure;

    @SerializedName("canonical_ids")
    private int canonicalIds;

    private List<Map<String, String>> results;

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

    public List<Map<String, String>> getResults() {
        return results;
    }
}
