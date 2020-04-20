package me.hp888.database.objects;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.Getter;
import lombok.Setter;
import me.hp888.database.DatabaseInitializer;
import me.hp888.database.api.database.DatabaseData;
import me.hp888.database.api.packet.PacketDirection;
import me.hp888.database.communication.handlers.ClientHandler;
import me.hp888.database.communication.handlers.packet.PacketDecoder;
import me.hp888.database.communication.handlers.packet.PacketEncoder;

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

        serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        serverBootstrap.childHandler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) {
                channel.pipeline()
                        .addLast("timeout", new ReadTimeoutHandler(15))
                        .addLast("packet-encoder", new PacketEncoder(PacketDirection.TO_CLIENT))
                        .addLast("packet-decoder", new PacketDecoder(PacketDirection.TO_SERVER))
                        .addLast("handler", new ClientHandler());
            }
        });

        serverBootstrap.bind(data.getHost(), data.getPort())
                .syncUninterruptibly()
                .addListener(future -> {
                    if (!future.isSuccess())
                        return;

                    DatabaseInitializer.getInstance().getLogger().info("Server bound on " + data.getHost() + ":" + data.getPort());
                });
    }
}