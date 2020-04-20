package me.hp888.database;

import com.google.common.collect.ImmutableSet;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import me.hp888.database.api.database.ConnectCallback;
import me.hp888.database.api.initialize.Initializer;
import me.hp888.database.api.packet.PacketDirection;
import me.hp888.database.api.packet.PacketRegistry;
import me.hp888.database.communication.handlers.ClientHandler;
import me.hp888.database.communication.handlers.packet.PacketDecoder;
import me.hp888.database.communication.handlers.packet.PacketEncoder;
import me.hp888.database.initialize.PacketInitializer;
import me.hp888.database.objects.DatabaseClient;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * @author hp888 on 19.04.2020.
 */

@Getter
public enum DatabaseDriver
{
    INSTANCE;

    private final ExecutorService executorService;
    private final EventLoopGroup eventExecutors;
    private final PacketRegistry packetRegistry;
    private final Logger logger;

    DatabaseDriver() {
        executorService = Executors.newSingleThreadExecutor();
        logger = Logger.getLogger("Database");
        packetRegistry = new PacketRegistry();

        eventExecutors = (Epoll.isAvailable())
                ? new EpollEventLoopGroup()
                : new NioEventLoopGroup();

        initialize(ImmutableSet.<Initializer>builder()
                .add(new PacketInitializer())
                .build()
        );
    }

    private void initialize(final ImmutableSet<Initializer> initializers) {
        initializers.forEach(initializer -> initializer.initialize(this));
    }

    public void connect(final DatabaseClient client, final ConnectCallback callback) {
        executorService.submit(() -> {
            final Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventExecutors);
            bootstrap.channel((eventExecutors instanceof EpollEventLoopGroup)
                    ? EpollSocketChannel.class
                    : NioSocketChannel.class
            );

            bootstrap.handler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel channel) throws Exception {
                    channel.pipeline()
                            .addLast("packet-encoder", new PacketEncoder(PacketDirection.TO_SERVER))
                            .addLast("packet-decoder", new PacketDecoder(PacketDirection.TO_CLIENT))
                            .addLast("handler", new ClientHandler(callback, client));
                }
            });

            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
            bootstrap.connect(client.getData().getHost(), client.getData().getPort())
                    .syncUninterruptibly();
        });
    }
}