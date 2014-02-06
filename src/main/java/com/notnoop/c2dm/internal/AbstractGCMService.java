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

import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicReference;

import com.notnoop.c2dm.GCMNotification;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;

import com.notnoop.c2dm.GCMService;
import org.apache.http.entity.StringEntity;

public abstract class AbstractGCMService implements GCMService {
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private static final String APPLICATION_JSON = "application/json";

    private final String serviceUri;
    private final String apiKey;

    private final RequestBuilder requestBuilder = new RequestBuilder();

    protected AbstractGCMService(String serviceUri, String apiKey) {
        this.serviceUri = serviceUri;
        this.apiKey = apiKey;
    }

    protected HttpPost postMessage(GCMNotification notification) {
        HttpPost method = new HttpPost(serviceUri);

        String jsonRequest = requestBuilder.build(notification);
        HttpEntity entity = new StringEntity(jsonRequest, UTF_8);

        method.setEntity(entity);
        method.addHeader("Content-Type", APPLICATION_JSON);
        method.addHeader("Authorization", "key=" + apiKey);
        return method;
    }

    @Override
    public void push(String payload) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }
}
