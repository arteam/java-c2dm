package com.notnoop.c2dm.internal;

import com.google.gson.Gson;
import com.notnoop.c2dm.GCMNotification;
import com.notnoop.c2dm.domain.CanonicalId;
import com.notnoop.c2dm.domain.GCMError;
import com.notnoop.c2dm.domain.GCMHttpResponse;
import com.notnoop.c2dm.domain.GCMResponse;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date: 09.07.13
 * Time: 15:14
 */
public class ResponseParser {

    private Gson gson = new Gson();

    @SuppressWarnings("unchecked")
    public GCMResponse parse(GCMNotification notification, HttpEntity entity) throws IOException {
        String content = EntityUtils.toString(entity, "UTF-8");
        GCMHttpResponse response;
        try {
            response = gson.fromJson(content, GCMHttpResponse.class);
        } catch (Exception e) {
            throw new IllegalStateException("Unable parse response: " + content, e);
        }

        int successLength = 0;
        int failureLength = 0;
        Map<GCMError, List<String>> failures = new HashMap<GCMError, List<String>>();
        List<CanonicalId> canonicalIds = new ArrayList<CanonicalId>();

        List<Map<String, String>> results = response.getResults();
        for (int i = 0; i < results.size(); i++) {
            Map<String, String> result = results.get(i);
            String error = result.get("error");
            if (error == null) {
                String registrationId = result.get("registration_id");
                if (registrationId == null) {
                    String messageId = result.get("message_id");
                    String newRegistrationId = result.get("new_registration_id");
                    canonicalIds.add(new CanonicalId(messageId, registrationId, newRegistrationId));
                }
                successLength++;
            } else {
                // Add registration id to error list
                GCMError gcmError = GCMError.of(error);
                List<String> errorRegistrationIds = failures.get(gcmError);
                if (errorRegistrationIds == null) {
                    errorRegistrationIds = new ArrayList<String>();
                    failures.put(gcmError, errorRegistrationIds);
                }
                errorRegistrationIds.add(notification.getRegistrationIds().get(i));
                failureLength++;
            }
        }

        return new GCMResponse(response.getMulticastId(), successLength, failureLength, failures, canonicalIds);

    }

}
