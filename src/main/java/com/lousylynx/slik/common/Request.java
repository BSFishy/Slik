package com.lousylynx.slik.common;

import com.lousylynx.slik.common.types.Method;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.Data;

@Data
public class Request {

    final FullHttpRequest actualRequest;

    final String path;
    final Method method;

    public Request(FullHttpRequest request) {
        actualRequest = request;

        path = request.uri();
        method = Method.valueOf(request.method());
    }
}
