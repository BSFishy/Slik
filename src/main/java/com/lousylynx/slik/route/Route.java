package com.lousylynx.slik.route;

import com.lousylynx.slik.common.types.Method;
import com.lousylynx.slik.route.parser.UrlParser;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
public class Route {

    // RULES:
    // [X] - input data
    // (X) - optional data
    @Getter
    private final String path;
    @Getter
    private final String pathRegex;
    @Getter
    private final Method method;
    @Getter
    private final ICallable call;
    @Getter
    @Setter
    private String name = "";

    private final List<String> allowed = new ArrayList<>();

    public Route(String path, Method method, ICallable call) {
        this.path = path;
        this.pathRegex = UrlParser.parse(path).asRegular();
        this.method = method;
        this.call = call;
    }

    public boolean pathMatches(String path) {
        return path.matches(pathRegex);
    }
}
