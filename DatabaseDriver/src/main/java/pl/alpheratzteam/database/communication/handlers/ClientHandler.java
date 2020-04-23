package pl.alpheratzteam.database.communication.handlers;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import pl.alpheratzteam.database.DatabaseDriver;
import pl.alpheratzteam.database.api.database.ConnectCallback;
import pl.alpheratzteam.database.api.database.DatabaseClient;
import pl.alpheratzteam.database.api.packet.Packet;
import pl.alpheratzteam.database.communication.packets.client.ClientAuthenticationPacket;
import java.util.Objects;

/**
 * @author hp888 on 23.04.2020.
 */

@RequiredArgsConstructor
public final class ClientHandler extends SimpleChannelInboundHandler<Packet>
{
    private final ConnectCallback callback;
    private final DatabaseClient client;

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) {
        client.setChannel(channelHandlerContext.channel());

        final ChannelFuture channelFuture = client.getChannel().writeAndFlush(new ClientAuthenticationPacket(client.getData().getDatabaseUser().getUser(), client.getData().getDatabaseUser().getPassword()));
        if (Objects.isNull(channelFuture))
            return;

        channelFuture.addListener(future -> {
            if (!future.isSuccess() || !future.isDone())
                return;

            DatabaseDriver.INSTANCE.getScheduler().buildTask(() -> callback.success(client))
                    .schedule();
        });
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Disconnected from database!");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) {
        packet.handlePacket(client.getPacketHandler());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        callback.error(cause);
    }
}