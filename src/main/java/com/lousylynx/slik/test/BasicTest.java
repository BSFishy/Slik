package com.lousylynx.slik.test;

import com.lousylynx.slik.Slik;
import com.lousylynx.slik.common.Request;
import com.lousylynx.slik.common.Response;
import com.lousylynx.slik.common.types.ContentType;
import com.lousylynx.slik.file.InternalFileReader;
import com.lousylynx.slik.route.ICallable;

public class BasicTest {

    public static void main(String[] args) {
        Slik.init(new Slik.EnvironmentBuilder()
                .setIp("localhost")
                .setPort(8070)
                .build());

        Slik.any("/[{page}]", new ICallable() {
            @Override
            public Response handle(Request request, Response response) {
                response.setType(ContentType.HTML);
//                response.append("This is a test. Click <a href=\"" + Slik.pathFor("home") + "\">here</a>.<br /> You inputted: <br /><ul>");
//
////                for(String page : request.getInputList("page")) {
////                    response.append("<li>" + page + "</li>");
////                }
//                response.append("<li>" + request.getInput("page") + "</li>");
//
//                response.append("</ul>");

                response.append(new InternalFileReader("/assets/slik/views/404.html"));

                return response;
            }
        }).setName("home");

//        Slik.any("/test/test", new ICallable() {
//            @Override
//            public Response handle(Request request, Response response) {
//                response.setType(ContentType.HTML);
//                response.append("This is yet another test. Click <a href=\"" + Slik.pathFor("home") + "\">here</a>.");
//                return response;
//            }
//        }).setName("test");

        Slik.start();
    }
}
