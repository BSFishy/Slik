package com.lousylynx.slik.route;

import com.lousylynx.slik.common.types.Method;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Route {

    @Getter
    private final String path;
    @Getter
    private final Method method;
    @Getter
    private final ICallable call;
    @Getter
    @Setter
    private String name = "";
}
