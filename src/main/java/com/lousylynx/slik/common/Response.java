package com.lousylynx.slik.common;

import com.lousylynx.slik.common.types.ContentType;
import com.lousylynx.slik.common.types.Status;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpVersion;
import lombok.Data;
import lombok.Setter;

@Data
public class Response {

    @Setter
    private Status status = Status.OK;
    @Setter
    private String body = "";
    @Setter
    private ContentType type = ContentType.TEXT;

    public void append(String append) {
        this.body += append;
    }

    public FullHttpResponse compile() {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                status.getValue(),
                Unpooled.copiedBuffer(body.getBytes())
        );

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, type.getLiteral());
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, body.length());

        return response;
    }
}
