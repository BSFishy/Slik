package com.lousylynx.slik.common.types;

import io.netty.handler.codec.http.HttpMethod;
import lombok.Getter;

public enum Method {
    OPTIONS("OPTIONS"),
    GET("GET"),
    HEAD("HEAD"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE"),
    TRACE("TRACE"),
    CONNECT("CONNECT"),
    ANY("ANY");

    @Getter
    final HttpMethod literal;
    @Getter
    final String name;

    Method(String name) {
        this.literal = HttpMethod.valueOf(name);
        this.name = name;
    }

    public static Method valueOf(HttpMethod method) {
        for (Method m : values()) {
            if (m.getName().equals(method.name())) {
                return m;
            }
        }

        return null;
    }

    public boolean equals(Method o) {
        return o.getName().equals(name) && o.getLiteral().equals(literal);
    }
}
