package com.lousylynx.slik.test;

import com.lousylynx.slik.Slik;
import com.lousylynx.slik.common.Request;
import com.lousylynx.slik.common.Response;
import com.lousylynx.slik.common.types.ContentType;
import com.lousylynx.slik.file.InternalFileReader;
import com.lousylynx.slik.route.ICallable;

public class Test2 {

    public static void main(String[] args) {
        Slik.init();

        Slik.any("/[{path}]", new ICallable() {
            @Override
            public Response handle(Request request, Response response) {
                response.setType(ContentType.HTML);
                response.append(new InternalFileReader("assets/slik/views/test.html"));

                response.append(request.getInput("path"));

                return response;
            }
        });

        Slik.start();
    }
}
