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
package com.notnoop.c2dm;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;

import com.notnoop.c2dm.internal.*;

/**
 * The class is used to create instances of {@link GCMService}.
 * <p/>
 * Note that this class is not synchronized.  If multiple threads access a
 * {@code GCMServiceBuilder} instance concurrently, and at least on of the
 * threads modifies one of the attributes structurally, it must be
 * synchronized externally.
 * <p/>
 * Starting a new {@code GCMService} is easy:
 * <p/>
 * <pre>
 *   GCMService service = GCM.newService()
 *                  .authToken("authtoken")
 *                  .build()
 * </pre>
 */
public class GCMServiceBuilder {
    public static String DEFAULT_GCM_SERVICE_URI = "https://android.googleapis.com/gcm/send";

    private String serviceUri = DEFAULT_GCM_SERVICE_URI;

    private String apiKey;

    private int pooledMax = 1;
    private ExecutorService executor = null;

    private boolean isQueued = false;
    private HttpHost proxy = null;
    private UsernamePasswordCredentials proxyAuth = null;
    private int timeout = -1;

    private GCMDelegate delegate;

    /**
     * Constructs a new instance of {@code GCMServiceBuilder}
     */
    GCMServiceBuilder() {
    }

    /**
     * Specify the address of the HTTP proxy the connection should
     * use.
     * <p/>
     * <p>Read the <a href="http://java.sun.com/javase/6/docs/technotes/guides/net/proxies.html">
     * Java Networking and Proxies</a> guide to understand the
     * proxies complexity.
     *
     * @param host the hostname of the HTTP proxy
     * @param port the port of the HTTP proxy server
     * @return this
     */
    public GCMServiceBuilder withHttpProxy(String host, int port) {
        proxy = new HttpHost(host, port);
        return this;
    }


    public GCMServiceBuilder withProxyAuth(String username, String password) {
        proxyAuth = new UsernamePasswordCredentials(username, password);
        return this;
    }

    /**
     * Specify the service URI to post Google GCM request to.
     * <p/>
     * This method should be used only for testing.  By default, the
     * notifications are posted to
     * {@linkplain https://android.apis.google.com/c2dm/send}, as specified
     * by the Google GCM.
     */
    public GCMServiceBuilder withServiceUri(String serviceUri) {
        this.serviceUri = serviceUri;
        return this;
    }

    public GCMServiceBuilder withApiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    /**
     * Constructs a pool of connections to the notification servers.
     */
    public GCMServiceBuilder asPool(int maxConnections) {
        return asPool(Executors.newFixedThreadPool(maxConnections), maxConnections);
    }

    /**
     * Constructs a pool of connections to the notification servers.
     * <p/>
     * Note: The maxConnections here is used as a hint to how many connections
     * get created.
     */
    public GCMServiceBuilder asPool(ExecutorService executor, int maxConnections) {
        this.pooledMax = maxConnections;
        this.executor = executor;
        return this;
    }

    /**
     * Constructs a new thread with a processing queue to process
     * notification requests.
     *
     * @return this
     */
    public GCMServiceBuilder asQueued() {
        this.isQueued = true;
        return this;
    }

    /**
     * Sets the timeout for the connection
     *
     * @param timeout the time out period in millis
     * @return this
     */
    public GCMServiceBuilder withTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public GCMServiceBuilder withDelegate(GCMDelegate delegate) {
        this.delegate = delegate;
        return this;
    }

    /**
     * Returns a fully initialized instance of {@link GCMService},
     * according to the requested settings.
     *
     * @return a new instance of GCMService
     */
    public GCMService build() {
        checkInitialization();

        // Client Configuration
        DefaultHttpClient client;
        if (pooledMax == 1) {
            client = new DefaultHttpClient();
        } else {
            client = new DefaultHttpClient(poolManager(pooledMax));
        }

        if (proxy != null) {
            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
            if (proxyAuth != null) {
                client.getCredentialsProvider().setCredentials(new AuthScope(proxy), proxyAuth);
            }
        }

        if (timeout > 0) {
            HttpParams params = client.getParams();
            params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
            params.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
        }

        // Configure service
        AbstractGCMService service;
        if (pooledMax == 1) {
            service = new GCMServiceImpl(client, serviceUri, apiKey, delegate);
        } else {
            service = new GCMPooledService(client, serviceUri, apiKey, executor, delegate);
        }

        if (isQueued) {
            service = new GCMQueuedService(service, serviceUri, apiKey);
        }

        service.start();
        return service;
    }

    private void checkInitialization() {
        if (apiKey == null) {
            throw new IllegalStateException("AuthToken is required");
        }
        if (pooledMax != 1 && executor == null) {
            throw new IllegalStateException("Executor service is required for pooled connections");
        }
    }


    private ClientConnectionManager poolManager(int maxConnections) {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        PoolingClientConnectionManager cm = new PoolingClientConnectionManager(schemeRegistry);
        cm.setMaxTotal(maxConnections);
        return cm;
    }
}
