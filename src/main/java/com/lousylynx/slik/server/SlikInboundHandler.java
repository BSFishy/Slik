package com.lousylynx.slik.server;

import com.lousylynx.slik.Slik;
import com.lousylynx.slik.common.Request;
import com.lousylynx.slik.common.Response;
import com.lousylynx.slik.common.types.ContentType;
import com.lousylynx.slik.common.types.Method;
import com.lousylynx.slik.file.InternalFileReader;
import com.lousylynx.slik.route.Route;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public class SlikInboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
        if (!(msg instanceof FullHttpRequest)) {
            super.channelRead(context, msg);
        }

        final FullHttpRequest request = (FullHttpRequest) msg;

        if (HttpUtil.is100ContinueExpected(request)) {
            send100Continue(context);
        }

        Request req = new Request(request);
        Response res = new Response();

        Route r = Slik.getRouter().getRoute(req.getPath(), Method.valueOf(request.method()));

        if (r != null) {
            req.setRoute(r);
            req.setupInputs();

            if (Slik.getEnv().isIncomingMessages())
                Slik.getLOG().info("Received and handled message: " + request.uri());

            Response newRes = r.getCall().handle(req, res);

            context.write(newRes.compile());
        } else {
            if (Slik.getEnv().isIncomingMessages())
                Slik.getLOG().info("Received a 404 message: " + request.uri());

            send404NotFound(context);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
            throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) throws Exception {
        Slik.getLOG().warn("There was an error processing the previous path");
        Slik.getLOG().warn(cause.getMessage(), cause);

        String text = cause.getMessage();

        FullHttpResponse res = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.INTERNAL_SERVER_ERROR,
                Unpooled.copiedBuffer(text.getBytes())
        );

        res.headers().set(HttpHeaderNames.CONTENT_TYPE, ContentType.HTML.getLiteral());
        res.headers().set(HttpHeaderNames.CONTENT_LENGTH, text.length());

        ctx.writeAndFlush(res);
    }


    private void send100Continue(final ChannelHandlerContext ctx) {
        FullHttpResponse res = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.CONTINUE
        );

        res.headers().set(HttpHeaderNames.CONTENT_TYPE, ContentType.HTML.getLiteral());
        //res.headers().set(HttpHeaderNames.CONTENT_LENGTH, text.length());

        ctx.write(res);
    }

    private void send404NotFound(final ChannelHandlerContext ctx) {
        String text = new InternalFileReader("assets/slik/views/404.html").read();

        FullHttpResponse res = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.NOT_FOUND,
                Unpooled.copiedBuffer(text, CharsetUtil.UTF_8)
        );

        res.headers().set(HttpHeaderNames.CONTENT_TYPE, ContentType.HTML.getLiteral());
        res.headers().set(HttpHeaderNames.CONTENT_LENGTH, text.length());

        ctx.write(res);
    }
}
