package com.lousylynx.slik.common;

import com.lousylynx.slik.common.types.Method;
import com.lousylynx.slik.route.Route;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class Request {

    final FullHttpRequest actualRequest;

    final String path;
    final Method method;

    @Getter
    @Setter
    private Route route;

    private final Map<String, List<String>> inputValues = new HashMap<>();

    public Request(FullHttpRequest request) {
        actualRequest = request;

        path = request.uri();
        method = Method.valueOf(request.method());
    }

    public void setupInputs() {
        Pattern pattern = Pattern.compile(route.getUrl().getRegular());
        Matcher matcher = pattern.matcher(path);

        while(matcher.find()) {
            for(int i = 1; i < matcher.groupCount(); i++) {
                String key = route.getUrl().getName(i);
                String value = matcher.group(i);

                List<String> values = inputValues.containsKey(key) ? inputValues.get(key) : new ArrayList<>();
                values.add(value);
                inputValues.put(key, values);
            }
        }
    }

    public List<String> getInput(String key) {
        return inputValues.get(key);
    }
}
