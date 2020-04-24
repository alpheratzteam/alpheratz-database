package pl.alpheratzteam.database;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonParser;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import pl.alpheratzteam.database.api.database.ConnectCallback;
import pl.alpheratzteam.database.api.database.DatabaseClient;
import pl.alpheratzteam.database.api.database.DatabaseRegistry;
import pl.alpheratzteam.database.api.initialize.Initializer;
import pl.alpheratzteam.database.api.packet.PacketDirection;
import pl.alpheratzteam.database.api.packet.PacketRegistry;
import pl.alpheratzteam.database.api.scheduler.Scheduler;
import pl.alpheratzteam.database.communication.handlers.*;
import pl.alpheratzteam.database.initialize.PacketInitializer;
import pl.alpheratzteam.database.scheduler.DatabaseScheduler;
import java.util.logging.Logger;

/**
 * @author hp888 on 23.04.2020.
 */

@Getter
public enum DatabaseDriver
{
    INSTANCE;

    private final DatabaseRegistry databaseRegistry;
    private final PacketRegistry packetRegistry;
    private final JsonParser jsonParser;
    private final Scheduler scheduler;
    private final Logger logger;

    DatabaseDriver() {
        logger = Logger.getLogger("AlpheratzDatabase");
        databaseRegistry = new DatabaseRegistry();
        packetRegistry = new PacketRegistry();
        scheduler = new DatabaseScheduler();
        jsonParser = new JsonParser();

        initialize(ImmutableSet.<Initializer>builder()
                .add(new PacketInitializer())
                .build()
        );
    }

    private void initialize(final ImmutableSet<Initializer> initializers) {
        initializers.forEach(initializer -> initializer.initialize(this));
    }

    public void connect(final DatabaseClient client, final ConnectCallback callback) {
        final Bootstrap bootstrap = new Bootstrap();
        bootstrap.group((Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup()));
        bootstrap.channel((Epoll.isAvailable())
                ? EpollSocketChannel.class
                : NioSocketChannel.class
        );

        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) {
                channel.pipeline()
                        .addLast("splitter", new FramingEncoder())
                        .addLast("prepender", new FramingDecoder())
                        .addLast("packet-encoder", new PacketEncoder(PacketDirection.TO_SERVER))
                        .addLast("packet-decoder", new PacketDecoder(PacketDirection.TO_CLIENT))
                        .addLast("handler", new ClientHandler(callback, client));
            }
        });

        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
        bootstrap.connect(client.getData().getHost(), client.getData().getPort())
                .syncUninterruptibly();
    }
}