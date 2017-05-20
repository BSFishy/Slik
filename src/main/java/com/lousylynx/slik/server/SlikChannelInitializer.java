package com.lousylynx.slik.server;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class SlikChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        channel.pipeline().addLast("codec", new HttpServerCodec());
        channel.pipeline().addLast("aggregator", new HttpObjectAggregator(512 * 1024));
        //channel.pipeline().addLast(new ChannelInboundHandlerAdapter());
        channel.pipeline().addLast(new SlikInboundHandler());
    }
}
