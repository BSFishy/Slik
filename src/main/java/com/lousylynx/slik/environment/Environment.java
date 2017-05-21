package com.lousylynx.slik.environment;

import com.lousylynx.slik.Slik.EnvironmentBuilder;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.Data;
import lombok.Setter;

@Data
public class Environment {

    private final String ip;
    private final int port;

    @Setter
    private ChannelFuture channel;
    private final EventLoopGroup masterGroup;
    private final EventLoopGroup slaveGroup;

    private final boolean debugMessages;
    private final boolean incomingMessages;

    public Environment(EnvironmentBuilder builder) {
        this.ip = builder.getIp();
        this.port = builder.getPort();

        this.masterGroup = new NioEventLoopGroup();
        this.slaveGroup = new NioEventLoopGroup();

        this.debugMessages = builder.isDebugMessages();
        this.incomingMessages = builder.isIncomingMessages();
    }
}
