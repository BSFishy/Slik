package com.lousylynx.slik;

import com.google.common.eventbus.EventBus;
import com.lousylynx.slik.common.types.Method;
import com.lousylynx.slik.environment.Environment;
import com.lousylynx.slik.event.ShutdownEvent;
import com.lousylynx.slik.route.ICallable;
import com.lousylynx.slik.route.Route;
import com.lousylynx.slik.route.Router;
import com.lousylynx.slik.server.SlikChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Data;
import lombok.Getter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Slik {

    @Getter
    private static Environment env;
    @Getter
    private static Router router = new Router();

    /**
     * The event bus for the Slik environment
     */
    public static final EventBus EVENT_BUS = new EventBus();

    public static final Logger LOG = LogManager.getLogger("Slik");

    /**
     * Initialize the Slik environment. This will setup all of the essential components needed for Slik to run, as well as allow you to add your own custom variables
     */
    public static void init() {
        init(new EnvironmentBuilder().build());
    }

    /**
     * Initialize the Slik environment. This will setup all of the essential components needed for Slik to run, as well as allow you to add your own custom variables
     * @param environment the environment to initialize with
     */
    public static void init(Environment environment) {
        env = environment;

        setupShutdownHook();

        if(env.isDebugMessages())
            LOG.info("Slik has been initialized");
    }

    public static void start() {
        final ServerBootstrap bootstrap = new ServerBootstrap()
                .group(env.getMasterGroup(), env.getSlaveGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new SlikChannelInitializer())
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        if(env.isDebugMessages())
            LOG.info("Slik is being started");

        try {
            env.setChannel(bootstrap.bind(env.getIp(), env.getPort()).sync());
        } catch (Exception e) {
            LOG.warn("There was an error starting the webserver.");
            LOG.log(Level.WARN, e.getMessage(), e.getCause());
        }
    }

    private static void setupShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            EVENT_BUS.post(new ShutdownEvent(env));

            Slik.shutdown();
        }));
    }

    /**
     * Shut down the server with this method. Everything will be gracefully shut down
     */
    public static void shutdown() {
        env.getMasterGroup().shutdownGracefully();
        env.getSlaveGroup().shutdownGracefully();

        try {
            env.getChannel().channel().closeFuture().sync();
        } catch (InterruptedException ignored) {}

        if(env.isDebugMessages())
            LOG.info("Slik has been successfully shut down");
    }

    public static Route get(String path, ICallable call) {
        return router.addRoute(path, Method.GET, call);
    }

    public static Route post(String path, ICallable call) {
        return router.addRoute(path, Method.POST, call);
    }

    public static Route patch(String path, ICallable call) {
        return router.addRoute(path, Method.PATCH, call);
    }

    public static Route put(String path, ICallable call) {
        return router.addRoute(path, Method.PUT, call);
    }

    public static Route any(String path, ICallable call) {
        return router.addRoute(path, Method.ANY, call);
    }

    public static String pathFor(String name) {
        return router.getRouteByName(name).getPath();
    }

    /**
     * This class allows you to setup your own custom values for your Slik instance
     */
    @Data
    public static class EnvironmentBuilder {

        private String ip = "127.0.0.1";
        private int port = 80;
        private boolean debugMessages = true;
        private boolean incomingMessages = true;

        /**
         * Sets a new value for the IP to run on
         * @param ip the new IP to set
         * @return the current EnvironmentBuilder instance
         */
        public EnvironmentBuilder setIp(String ip) {
            this.ip = ip;
            return this;
        }

        /**
         * Sets a new value for the port
         * @param port the new port value
         * @return the current EnvironmentBuilder instance
         */
        public EnvironmentBuilder setPort(int port) {
            this.port = port;
            return this;
        }

        public EnvironmentBuilder setDebugMessages(boolean m) {
            debugMessages = m;
            return this;
        }

        public EnvironmentBuilder setIncomingMessages(boolean m) {
            incomingMessages = m;
            return this;
        }

        /**
         * Build the builder to make an {@link Environment}
         * @return the built {@link Environment}
         */
        public Environment build() {
            return new Environment(this);
        }
    }
}
