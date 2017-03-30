package com.iotwear.wear.gui.handlers;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

public class RetryHandler implements HttpRequestRetryHandler {
    
	@Override
	public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
    if (exception == null) {
        throw new IllegalArgumentException("Exception parameter may not be null");
    }
    
    if (context == null) {
        throw new IllegalArgumentException("HTTP context may not be null");
    }
    
    if (executionCount > 3) {
        return false;
    }
    
    if (exception instanceof NoHttpResponseException) {
        // Retry if the server dropped connection on us
        return true;
    }
    
		if (exception instanceof SocketException) {
			// Retry if the server closed socket on us
			return true;
		}
		
    if (exception instanceof InterruptedIOException) {
        // Timeout
        return false;
    }
    
    if (exception instanceof UnknownHostException) {
        // Unknown host
        return false;
    }
    
    if (exception instanceof SSLHandshakeException) {
        // SSL handshake exception
        return false;
    }
    
    Boolean b = (Boolean)context.getAttribute(ExecutionContext.HTTP_REQ_SENT);
    boolean sent = (b != null && b.booleanValue());
    if (!sent) {
        // Retry if the request has not been sent fully or
        // if it's OK to retry methods that have been sent
        return true;
    }

    HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
		boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
		if (idempotent) {
			// Retry if the request is considered idempotent
			return true;
		}

		// otherwise do not retry
    return false;
	}
}
