package pl.alpheratzteam.database.communication.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author Unix on 19.10.2019.
 */
public class FramingEncoder extends MessageToByteEncoder<ByteBuf> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, ByteBuf byteBuf2) {
        byteBuf2.ensureWritable(4 + byteBuf.readableBytes());
        byteBuf2.writeInt(byteBuf.readableBytes());
        byteBuf2.writeBytes(byteBuf, byteBuf.readerIndex(), byteBuf.readableBytes());
    }
}