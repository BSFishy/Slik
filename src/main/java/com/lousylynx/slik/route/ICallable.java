package com.lousylynx.slik.route;

import com.lousylynx.slik.common.Request;
import com.lousylynx.slik.common.Response;

public interface ICallable {

    default Response handle(Request request, Response response){
        return response;
    }
}
