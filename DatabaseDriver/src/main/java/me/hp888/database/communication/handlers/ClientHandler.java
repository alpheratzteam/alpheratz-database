package me.hp888.database.communication.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import me.hp888.database.api.database.ConnectCallback;
import me.hp888.database.api.packet.Packet;
import me.hp888.database.communication.packets.client.ClientAuthenticationPacket;
import me.hp888.database.objects.DatabaseClient;

/**
 * @author hp888 on 19.04.2020.
 */

@RequiredArgsConstructor
public final class ClientHandler extends SimpleChannelInboundHandler<Packet>
{
    private final ConnectCallback callback;
    private final DatabaseClient client;

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        client.setChannel(channelHandlerContext.channel());
        client.sendPacket(new ClientAuthenticationPacket(client.getData().getDatabaseUser().getUser(), client.getData().getDatabaseUser().getPassword()));
        callback.success(client);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) throws Exception {
        packet.handlePacket(client.getPacketHandler());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        callback.error(cause);
    }
}