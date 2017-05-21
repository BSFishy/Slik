package com.lousylynx.slik.common.types;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Getter;

public enum Status {
    CONTINUE(100),
    OK(200),
    CREATED(201),
    ACCEPTED(202),
    MOVED_PERMANENTLY(301),
    FOUND(302),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    PAYMENT_REQUIRED(402),
    FORBIDDEN(403),
    NOT_FOUND(404),
    BAD_GATEWAY(502);

    @Getter
    private final String name;
    @Getter
    private final HttpResponseStatus value;
    @Getter
    private final int code;

    Status(int value) {
        this.value = HttpResponseStatus.valueOf(value);
        this.code = value;
        this.name = this.value.reasonPhrase();
    }
}
