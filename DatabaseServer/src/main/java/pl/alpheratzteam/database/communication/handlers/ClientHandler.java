package pl.alpheratzteam.database.communication.handlers;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import pl.alpheratzteam.database.DatabaseInitializer;
import pl.alpheratzteam.database.api.packet.Packet;
import pl.alpheratzteam.database.communication.packets.client.ClientAuthenticationPacket;
import pl.alpheratzteam.database.api.database.DatabaseClient;
import java.util.logging.Level;

/**
 * @author hp888 on 19.04.2020.
 */

public final class ClientHandler extends SimpleChannelInboundHandler<Packet>
{
    private final DatabaseClient client;

    public ClientHandler(final Channel channel) {
        client = new DatabaseClient(channel);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) {
        if (DatabaseInitializer.INSTANCE.getServer().getData().isAuthenticationEnabled()
                && ((!client.isAuthenticated() && !(packet instanceof ClientAuthenticationPacket)) || (client.isAuthenticated() && packet instanceof ClientAuthenticationPacket))) {

            channelHandlerContext.close();
            return;
        }

        packet.handlePacket(client.getPacketHandler());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause) {
        channelHandlerContext.close();
        DatabaseInitializer.INSTANCE.getLogger().log(Level.SEVERE, "Exception thrown!", cause);
    }
}