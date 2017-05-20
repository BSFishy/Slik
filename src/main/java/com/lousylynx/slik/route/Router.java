package com.lousylynx.slik.route;

import com.lousylynx.slik.Slik;
import com.lousylynx.slik.common.types.Method;

import java.util.ArrayList;
import java.util.List;

public class Router {

    private List<Route> routes = new ArrayList<>();

    public Route addRoute(String path, Method method, ICallable call) {
        Route r = new Route(path, method, call);
        routes.add(r);

        Slik.LOG.info("Route \"" + method.getName() + "\" for path has been added: " + path);

        return r;
    }

    public List<Route> getRoutes(Method method) {
        List<Route> result = new ArrayList<>();

        for(Route r : routes) {
            if(r.getMethod().equals(method)) {
                result.add(r);
            }
        }

        return result;
    }

    public Route getRoute(String path) {
        for(Route r : routes) {
            if(r.getPath().equals(path)) {
                return r;
            }
        }

        return null;
    }

    public Route getRouteByName(String name) {
        for(Route r : routes) {
            if(r.getName().equals(name)) {
                return r;
            }
        }

        return null;
    }
}
