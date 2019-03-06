package com.wmp.docparser.client;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.protocol.HttpContext;

public class SettableConnectionKeepAliveStrategy extends DefaultConnectionKeepAliveStrategy {

    private long maxKeepAlive;

    SettableConnectionKeepAliveStrategy(long maxKeepAlive) {
        this.maxKeepAlive = maxKeepAlive;
    }

    @Override
    public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
        long keepAlive = super.getKeepAliveDuration(response, context);

        if (maxKeepAlive > 0) {
            keepAlive = (keepAlive <= 0) ? maxKeepAlive : Math.min(maxKeepAlive, keepAlive);
        }
        return keepAlive;
    }
}
