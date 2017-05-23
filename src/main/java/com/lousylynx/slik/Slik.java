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
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.InputStream;

public class Slik {

    @Getter
    private static Environment env;
    @Getter
    private static Router router = new Router();

    /**
     * The event bus for the Slik environment
     */
    public static final EventBus EVENT_BUS = new EventBus();
    @Getter
    private static Logger LOG;
    public static final String assetsFolder = "assets/slik/";

    /**
     * Initialize the Slik environment. This will setup all of the essential components needed for Slik to run, as well as allow you to add your own custom variables
     */
    public static void init() {
        init(new EnvironmentBuilder().build());
    }

    /**
     * Initialize the Slik environment. This will setup all of the essential components needed for Slik to run, as well as allow you to add your own custom variables
     *
     * @param environment the environment to initialize with
     */
    public static void init(Environment environment) {
        env = environment;

        setupShutdownHook();
        setupLogger();

        if (env.isDebugMessages())
            LOG.info("Slik has been initialized");
    }

    private static void setupLogger() {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream is = classLoader.getResourceAsStream(assetsFolder + "log4j2.xml");

            ConfigurationSource source = new ConfigurationSource(is);
            Configurator.initialize(null, source);

            LOG = LogManager.getLogger("Slik");
        } catch (Exception e) {
            LOG = LogManager.getLogger();
            //System.out.println("Failed to initialize the logger");
            LOG.error("Failed to initialize the logger");
            LOG.log(Level.ERROR, e.getMessage(), e);
        }
    }

    private static void setupShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            EVENT_BUS.post(new ShutdownEvent(env));

            Slik.shutdown();
        }));
    }

    public static void start() {
        final ServerBootstrap bootstrap = new ServerBootstrap()
                .group(env.getMasterGroup(), env.getSlaveGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new SlikChannelInitializer())
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        if (env.isDebugMessages())
            LOG.info("Slik is being started");

        try {
            env.setChannel(bootstrap.bind(env.getIp(), env.getPort()).sync());
        } catch (Exception e) {
            LOG.error("There was an error starting the webserver.");
            LOG.log(Level.ERROR, e.getMessage(), e.getCause());
            LOG.error("Shutting down");

            System.exit(1);
        }
    }

    /**
     * Shut down the server with this method. Everything will be gracefully shut down
     */
    public static void shutdown() {
        env.getMasterGroup().shutdownGracefully();
        env.getSlaveGroup().shutdownGracefully();

        try {
            env.getChannel().channel().closeFuture().sync();
        } catch (InterruptedException ignored) {
        }

        if (env.isDebugMessages())
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
        try {
            return router.getRouteByName(name).getPath();
        }catch(Exception e) {
            LOG.error("Tried to get an nonexistent route: " + name);

            return "/";
        }
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
         *
         * @param ip the new IP to set
         * @return the current EnvironmentBuilder instance
         */
        public EnvironmentBuilder setIp(String ip) {
            this.ip = ip;
            return this;
        }

        /**
         * Sets a new value for the port
         *
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
         *
         * @return the built {@link Environment}
         */
        public Environment build() {
            return new Environment(this);
        }
    }
}
