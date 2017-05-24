package com.lousylynx.slik.route;

import com.lousylynx.slik.common.types.Method;
import com.lousylynx.slik.route.parser.Url;
import com.lousylynx.slik.route.parser.UrlParser;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class Route {

    @Getter
    private final String path;
    @Getter
    private final Url url;
    @Getter
    private final Method method;
    @Getter
    private final ICallable call;
    @Getter
    private String name = "";

    private final List<String> allowed = new ArrayList<>();

    public Route(String path, Method method, ICallable call) {
        this.path = path;
        this.url = UrlParser.parse(path);
        this.method = method;
        this.call = call;
    }

    public boolean pathMatches(String path) {
        if(url.isMatchAll()) {
            final Pattern pattern = Pattern.compile(url.getRegular());
            final Matcher matcher = pattern.matcher(path);

            while(matcher.find()){
                return true;
            }

            return false;
        }

        return path.matches(url.getRegular());
    }

    public Route setName(String name) {
        this.name = name;
        return this;
    }
}
