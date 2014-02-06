package com.notnoop.c2dm.internal;

import com.google.gson.Gson;
import com.google.gson.internal.StringMap;
import com.notnoop.c2dm.GCMResponse;
import com.notnoop.c2dm.GCMResponseStatus;
import com.notnoop.c2dm.exceptions.GCMException;
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
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        switch (statusCode) {
            case 200: {
                return toResponse(httpResponse.getEntity());
            }
            case 503:
                return GCMResponse.withStatus(GCMResponseStatus.SERVER_UNAVAILABLE);
            case 400:
                return GCMResponse.withStatus(GCMResponseStatus.INVALID_REQUEST);
            case 401:
                return GCMResponse.withStatus(GCMResponseStatus.INVALID_AUTHENTICATION);
            default:
                return GCMResponse.withStatus(GCMResponseStatus.UNKNOWN_ERROR);
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
    private GCMResponse toResponse(HttpEntity entity) {
        String content;
        try {
            content = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            throw new NetworkIOException(e);
        }
        try {
            return gson.fromJson(content, GCMResponse.class);
        } catch (Exception e) {
            throw new IllegalStateException("Unable parse response: " + content, e);
        }
    }
}
