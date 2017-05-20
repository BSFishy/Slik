package com.lousylynx.slik.common.types;

import lombok.Getter;

public enum ContentType {

    TEXT("text/plain"),
    HTML("text/html"),
    CSS("text/css"),
    JS("text/javascript"),

    GIF("image/gif"),
    PNG("image/png"),
    JPEG("image/jpeg")
    ;

    @Getter
    private String literal;

    ContentType(String type) {
        literal = type;
    }
}
