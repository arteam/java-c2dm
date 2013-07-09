package com.notnoop.c2dm.internal;

import com.google.gson.Gson;
import com.google.gson.internal.StringMap;
import com.notnoop.c2dm.C2DMResponse;
import com.notnoop.c2dm.C2DMResponseStatus;
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
    public C2DMResponse parse(HttpResponse httpResponse) {
        StringMap jResponse = toResponse(httpResponse.getEntity());

        C2DMResponseStatus status = getStatus(httpResponse, jResponse);
        long multicastId = Long.parseLong(jResponse.get("multicast_id").toString());
        int success = (Integer) jResponse.get("success");
        int failure = (Integer) jResponse.get("failure");
        int canonicalIds = (Integer) jResponse.get("canonical_ids");
        StringMap<String> results = (StringMap<String>) jResponse.get("results");
        return new C2DMResponse(multicastId, success, failure, canonicalIds, results, status);
    }


    public C2DMResponseStatus getStatus(HttpResponse httpResponse, StringMap jsonResponse) {
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        switch (statusCode) {
            case 503:
                return C2DMResponseStatus.SERVER_UNAVAILABLE;
            case 400:
                return C2DMResponseStatus.INVALID_REQUEST;
            case 401:
                return C2DMResponseStatus.INVALID_AUTHENTICATION;
            case 200: {
                StringMap result = (StringMap) jsonResponse.get("result");
                if (result.containsKey("message_id")) {
                    return C2DMResponseStatus.SUCCESSFUL;
                }
                if (result.containsKey("registration_id")) {
                    return C2DMResponseStatus.INVALID_REGISTRATION;
                }
                if (result.containsKey("error")) {
                    String error = (String) result.get("error");
                    return C2DMResponseStatus.of(error);
                }
                return C2DMResponseStatus.UNKNOWN_ERROR;
            }
            default:
                return C2DMResponseStatus.UNKNOWN_ERROR;
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
            String charset = "UTF-8";
            String content = EntityUtils.toString(entity, charset);
            return gson.fromJson(content, StringMap.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
