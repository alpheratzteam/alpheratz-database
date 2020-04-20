package me.hp888.database.communication.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.hp888.database.DatabaseInitializer;
import me.hp888.database.api.packet.Packet;
import me.hp888.database.communication.packets.client.ClientAuthenticationPacket;
import me.hp888.database.objects.DatabaseClient;

/**
 * @author hp888 on 19.04.2020.
 */

public final class ClientHandler extends SimpleChannelInboundHandler<Packet>
{
    private final DatabaseClient client;

    public ClientHandler() {
        client = new DatabaseClient();
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) {
        client.setChannel(channelHandlerContext.channel());
        System.out.println("Client was connected!");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) {
        if (DatabaseInitializer.getInstance().getServer().getData().isAuthenticationEnabled()) {
            if (!client.isAuthenticated() && !(packet instanceof ClientAuthenticationPacket)) {
                System.err.println("Don't received any auth packet!");
                channelHandlerContext.close();
                return;
            } else if (client.isAuthenticated() && packet instanceof ClientAuthenticationPacket) {
                System.err.println("Received too many auth packets!");
                channelHandlerContext.close();
                return;
            }
        }

        packet.handlePacket(client.getPacketHandler());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause) {
        cause.printStackTrace();
        channelHandlerContext.close();
    }
}