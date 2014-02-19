/*
 * Copyright 2011, Mahmood Ali.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above
 *     copyright notice, this list of conditions and the following disclaimer
 *     in the documentation and/or other materials provided with the
 *     distribution.
 *   * Neither the name of Mahmood Ali. nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.notnoop.c2dm.internal;

import java.io.IOException;
import java.nio.charset.Charset;

import com.notnoop.c2dm.*;
import com.notnoop.c2dm.domain.GCMError;
import com.notnoop.c2dm.domain.GCMResponse;
import com.notnoop.c2dm.domain.GCMStatus;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import com.notnoop.c2dm.exceptions.NetworkIOException;

public class GCMServiceImpl implements GCMService {

    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private static final String APPLICATION_JSON = "application/json";

    private final String serviceUri;
    private final String apiKey;

    private final HttpClient httpClient;
    private final GCMDelegate delegate;

    private final RequestBuilder requestBuilder = new RequestBuilder();
    private final ResponseParser responseParser = new ResponseParser();

    public GCMServiceImpl(String serviceUri, String apiKey, HttpClient httpClient, GCMDelegate delegate) {
        this.serviceUri = serviceUri;
        this.apiKey = apiKey;
        this.httpClient = httpClient;
        this.delegate = delegate;
    }

    public void push(GCMNotification message) {
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(postMessage(message));
            if (delegate != null) {
                GCMStatus status = GCMStatus.valueOf(httpResponse.getStatusLine().getStatusCode());
                if (status == GCMStatus.SUCCESSFUL) {
                    GCMResponse response = responseParser.parse(message, httpResponse.getEntity());
                    delegate.messageSent(message, response);
                } else {
                    delegate.messageFailed(message, status);
                }
            }
        } catch (IOException e) {
            throw new NetworkIOException("Unable send request to " + serviceUri, e);
        } finally {
            try {
                if (httpResponse != null) EntityUtils.consume(httpResponse.getEntity());
            } catch (IOException e) {
                System.err.println("Unable close response " + e);
            }
        }
    }

    protected HttpPost postMessage(GCMNotification notification) {
        HttpPost method = new HttpPost(serviceUri);

        String jsonRequest = requestBuilder.build(notification);
        method.setEntity(new StringEntity(jsonRequest, UTF_8));
        method.addHeader("Content-Type", APPLICATION_JSON);
        method.addHeader("Authorization", "key=" + apiKey);
        return method;
    }

    @Override
    public void stop() {
        httpClient.getConnectionManager().shutdown();
    }

}
