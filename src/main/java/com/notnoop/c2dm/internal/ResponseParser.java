package com.notnoop.c2dm.internal;

import com.notnoop.c2dm.C2DMDelegate;
import com.notnoop.c2dm.C2DMNotification;
import com.notnoop.c2dm.C2DMResponse;
import com.notnoop.c2dm.C2DMResponseStatus;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Date: 09.07.13
 * Time: 15:14
 */
public class ResponseParser {

    public C2DMResponse parse(HttpResponse response) {
        List<NameValuePair> pairs;
        try {
            pairs = toPairs(response.getEntity());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        C2DMResponseStatus status = getStatus(response, pairs);
        if (!pairs.isEmpty()) {
            String messageId = pairs.get(0).getValue();
            return new C2DMResponse(status, messageId);
        } else {
            return new C2DMResponse(status);
        }
    }


    public C2DMResponseStatus getStatus(HttpResponse response, List<NameValuePair> pairs) {
        int statusCode = response.getStatusLine().getStatusCode();
        switch (statusCode) {
            case 503:
                return C2DMResponseStatus.SERVER_UNAVAILABLE;
            case 401:
                return C2DMResponseStatus.INVALID_AUTHENTICATION;
            case 200: {
                assert pairs.size() == 1;

                NameValuePair entry = pairs.get(0);
                if ("id".equals(entry.getName())) {
                    return C2DMResponseStatus.SUCCESSFUL;
                }

                assert "Error".equals(entry.getName());
                return C2DMResponseStatus.of(entry.getValue());
            }
            default:
                return C2DMResponseStatus.UNKNOWN_ERROR;
        }
    }

    /**
     * Workaround Google responding with Content-Type being
     * "text/plain" rather than "application/x-www-form-urlencoded"
     * as expected by Apache HTTP.
     */
    private List<NameValuePair> toPairs(HttpEntity entity) throws ParseException, IOException {
        String charset = "UTF-8";
        String content = EntityUtils.toString(entity, charset);
        List<NameValuePair> result = new ArrayList<NameValuePair>();
        if (content != null && content.length() > 0) {
            result = new ArrayList<NameValuePair>();
            URLEncodedUtils.parse(result, new Scanner(content), charset);
        }
        return result;
    }
}
