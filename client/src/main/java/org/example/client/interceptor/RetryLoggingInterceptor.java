package org.example.client.interceptor;

import io.grpc.*;

import java.util.logging.Logger;

public class RetryLoggingInterceptor implements ClientInterceptor {
    Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method,
            CallOptions callOptions,
            Channel next) {
        return new ForwardingClientCall.SimpleForwardingClientCall<>(next.newCall(method, callOptions)) {
            private int attempt = 1;

            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                logger.warning("🔁 Attempt #" + attempt + " for method: " + method.getFullMethodName());
                attempt++;
                super.start(responseListener, headers);
            }
        };
    }
}
