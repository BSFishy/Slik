package com.lousylynx.slik.route;

import com.lousylynx.slik.Slik;
import com.lousylynx.slik.common.types.Method;
import lombok.Data;
import lombok.Setter;

@Data
public abstract class Group {

    @Setter
    private String prefix;

    public abstract void setup();

    public Route get(String path, ICallable call) {
        return Slik.getRouter().addRoute(prefix + path, Method.GET, call);
    }

    public Route post(String path, ICallable call) {
        return Slik.getRouter().addRoute(prefix + path, Method.POST, call);
    }

    public Route patch(String path, ICallable call) {
        return Slik.getRouter().addRoute(prefix + path, Method.PATCH, call);
    }

    public Route put(String path, ICallable call) {
        return Slik.getRouter().addRoute(prefix + path, Method.PUT, call);
    }

    public Route any(String path, ICallable call) {
        return Slik.getRouter().addRoute(prefix + path, Method.ANY, call);
    }
}
