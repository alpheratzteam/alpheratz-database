package pl.alpheratzteam.database.communication.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author Unix on 19.10.2019.
 */

public class FramingDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        while (byteBuf.readableBytes() >= 4) {
            byteBuf.markReaderIndex();

            final int toRead = byteBuf.readInt();
            if (byteBuf.readableBytes() < toRead) {
                byteBuf.resetReaderIndex();
                return;
            }

            list.add(byteBuf.readBytes(toRead));
        }
    }
}