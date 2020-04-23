package pl.alpheratzteam.database.api.database;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import lombok.Setter;
import pl.alpheratzteam.database.DatabaseInitializer;
import pl.alpheratzteam.database.api.packet.PacketDirection;
import pl.alpheratzteam.database.communication.handlers.ClientHandler;
import pl.alpheratzteam.database.communication.handlers.PacketDecoder;
import pl.alpheratzteam.database.communication.handlers.PacketEncoder;

/**
 * @author hp888 on 18.04.2020.
 */

@Getter
@Setter
public final class DatabaseServer
{
    private final EventLoopGroup eventExecutors;

    public DatabaseServer() {
        eventExecutors = (Epoll.isAvailable())
                ? new EpollEventLoopGroup()
                : new NioEventLoopGroup();
    }

    private DatabaseData data;

    public void listen() {
        final ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(eventExecutors);
        serverBootstrap.channel((eventExecutors instanceof EpollEventLoopGroup)
                ? EpollServerSocketChannel.class
                : NioServerSocketChannel.class
        );

        serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) {
                channel.pipeline()
                     //   .addLast("timeout", new ReadTimeoutHandler(15))
                        .addLast("packet-decoder", new PacketDecoder(PacketDirection.TO_SERVER))
                        .addLast("packet-encoder", new PacketEncoder(PacketDirection.TO_CLIENT))
                        .addLast("handler", new ClientHandler(channel));
            }
        });

        serverBootstrap.bind(data.getHost(), data.getPort())
                .syncUninterruptibly()
                .addListener(future -> {
                    if (!future.isSuccess())
                        return;

                    DatabaseInitializer.INSTANCE.getLogger().info("Server bound on " + data.getHost() + ":" + data.getPort());
                });
    }
}