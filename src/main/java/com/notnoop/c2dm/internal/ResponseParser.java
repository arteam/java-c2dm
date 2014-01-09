package com.notnoop.c2dm.internal;

import com.google.gson.Gson;
import com.google.gson.internal.StringMap;
import com.notnoop.c2dm.GCMResponse;
import com.notnoop.c2dm.GCMResponseStatus;
import com.notnoop.c2dm.exceptions.NetworkIOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Date: 09.07.13
 * Time: 15:14
 */
public class ResponseParser {

    private Gson gson = new Gson();

    @SuppressWarnings("unchecked")
    public GCMResponse parse(HttpResponse httpResponse) {
        StringMap jsonResponse = toResponse(httpResponse.getEntity());

        GCMResponseStatus status = getStatus(httpResponse, jsonResponse);
        long multicastId = Long.parseLong(jsonResponse.get("multicast_id").toString());
        int success = (Integer) jsonResponse.get("success");
        int failure = (Integer) jsonResponse.get("failure");
        int canonicalIds = (Integer) jsonResponse.get("canonical_ids");
        StringMap<String> results = (StringMap<String>) jsonResponse.get("results");
        return new GCMResponse(multicastId, success, failure, canonicalIds, results, status);
    }


    public GCMResponseStatus getStatus(HttpResponse httpResponse, StringMap jsonResponse) {
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        switch (statusCode) {
            case 503:
                return GCMResponseStatus.SERVER_UNAVAILABLE;
            case 400:
                return GCMResponseStatus.INVALID_REQUEST;
            case 401:
                return GCMResponseStatus.INVALID_AUTHENTICATION;
            case 200: {
                StringMap result = (StringMap) jsonResponse.get("result");
                if (result.containsKey("message_id")) {
                    return GCMResponseStatus.SUCCESSFUL;
                }
                if (result.containsKey("registration_id")) {
                    return GCMResponseStatus.INVALID_REGISTRATION;
                }
                if (result.containsKey("error")) {
                    String error = (String) result.get("error");
                    return GCMResponseStatus.of(error);
                }
                return GCMResponseStatus.UNKNOWN_ERROR;
            }
            default:
                return GCMResponseStatus.UNKNOWN_ERROR;
        }
    }

    /**
     * {"multicast_id": 108,
     * "success": 1,
     * "failure": 0,
     * "canonical_ids": 0,
     * "results": [
     * { "message_id": "1:08" }
     * ]}
     */
    private StringMap toResponse(HttpEntity entity) {
        try {
            String content = EntityUtils.toString(entity, "UTF-8");
            return gson.fromJson(content, StringMap.class);
        } catch (IOException e) {
            throw new NetworkIOException(e);
        }
    }
}
