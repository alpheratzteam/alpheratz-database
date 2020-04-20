package pl.alpheratzteam.database.api.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import io.netty.util.ByteProcessor;
import lombok.RequiredArgsConstructor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author hp888 on 19.04.2020.
 */

@RequiredArgsConstructor
public final class PacketBuffer extends ByteBuf
{
    private final ByteBuf byteBuf;

    public PacketBuffer writeByteArray(final byte[] array) {
        this.writeInt(array.length);
        this.writeBytes(array);
        return this;
    }

    public byte[] readByteArray() {
        return this.readByteArray(this.readableBytes());
    }

    public byte[] readByteArray(final int maxLength) {
        final int i = this.readInt();
        if (i > maxLength)
            throw new DecoderException("ByteArray with size " + i + " is bigger than allowed " + maxLength);

        final byte[] bytes = new byte[i];
        this.readBytes(bytes);
        return bytes;
    }

    public <T extends Enum<T>> T readEnumValue(final Class<T> enumClass) {
        return enumClass.getEnumConstants()[this.readInt()];
    }

    public PacketBuffer writeEnumValue(final Enum<?> value) {
        this.writeInt(value.ordinal());
        return this;
    }

    public PacketBuffer writeUuid(final UUID uuid) {
        this.writeLong(uuid.getMostSignificantBits());
        this.writeLong(uuid.getLeastSignificantBits());
        return this;
    }

    public UUID readUuid() {
        return new UUID(this.readLong(), this.readLong());
    }

    public String readStringFromBuffer(final int maxLength) {
        final int i = this.readInt();
        if (i > maxLength * 4)
            throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + i + " > " + maxLength * 4 + ")");

        if (i < 0)
            throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");

        final String s = this.toString(this.readerIndex(), i, StandardCharsets.UTF_8);
        this.readerIndex(this.readerIndex() + i);

        if (s.length() > maxLength)
            throw new DecoderException("The received string length is longer than maximum allowed (" + i + " > " + maxLength + ")");

        return s;
    }

    public PacketBuffer writeString(final String string) {
        final byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        if (bytes.length > Short.MAX_VALUE)
            throw new EncoderException("String too big (was " + bytes.length + " bytes encoded, max " + Short.MAX_VALUE + ")");

        this.writeInt(bytes.length);
        this.writeBytes(bytes);
        return this;
    }

    public int capacity()
    {
        return byteBuf.capacity();
    }

    public ByteBuf capacity(int p_capacity_1_)
    {
        return byteBuf.capacity(p_capacity_1_);
    }

    public int maxCapacity()
    {
        return byteBuf.maxCapacity();
    }

    public ByteBufAllocator alloc()
    {
        return byteBuf.alloc();
    }

    public ByteOrder order()
    {
        return byteBuf.order();
    }

    public ByteBuf order(ByteOrder p_order_1_)
    {
        return byteBuf.order(p_order_1_);
    }

    public ByteBuf unwrap()
    {
        return byteBuf.unwrap();
    }

    public boolean isDirect()
    {
        return byteBuf.isDirect();
    }

    public boolean isReadOnly()
    {
        return byteBuf.isReadOnly();
    }

    public ByteBuf asReadOnly()
    {
        return byteBuf.asReadOnly();
    }

    public int readerIndex()
    {
        return byteBuf.readerIndex();
    }

    public ByteBuf readerIndex(int p_readerIndex_1_)
    {
        return byteBuf.readerIndex(p_readerIndex_1_);
    }

    public int writerIndex()
    {
        return byteBuf.writerIndex();
    }

    public ByteBuf writerIndex(int p_writerIndex_1_)
    {
        return byteBuf.writerIndex(p_writerIndex_1_);
    }

    public ByteBuf setIndex(int p_setIndex_1_, int p_setIndex_2_)
    {
        return byteBuf.setIndex(p_setIndex_1_, p_setIndex_2_);
    }

    public int readableBytes()
    {
        return byteBuf.readableBytes();
    }

    public int writableBytes()
    {
        return byteBuf.writableBytes();
    }

    public int maxWritableBytes()
    {
        return byteBuf.maxWritableBytes();
    }

    public boolean isReadable()
    {
        return byteBuf.isReadable();
    }

    public boolean isReadable(int p_isReadable_1_)
    {
        return byteBuf.isReadable(p_isReadable_1_);
    }

    public boolean isWritable()
    {
        return byteBuf.isWritable();
    }

    public boolean isWritable(int p_isWritable_1_)
    {
        return byteBuf.isWritable(p_isWritable_1_);
    }

    public ByteBuf clear()
    {
        return byteBuf.clear();
    }

    public ByteBuf markReaderIndex()
    {
        return byteBuf.markReaderIndex();
    }

    public ByteBuf resetReaderIndex()
    {
        return byteBuf.resetReaderIndex();
    }

    public ByteBuf markWriterIndex()
    {
        return byteBuf.markWriterIndex();
    }

    public ByteBuf resetWriterIndex()
    {
        return byteBuf.resetWriterIndex();
    }

    public ByteBuf discardReadBytes()
    {
        return byteBuf.discardReadBytes();
    }

    public ByteBuf discardSomeReadBytes()
    {
        return byteBuf.discardSomeReadBytes();
    }

    public ByteBuf ensureWritable(int p_ensureWritable_1_)
    {
        return byteBuf.ensureWritable(p_ensureWritable_1_);
    }

    public int ensureWritable(int p_ensureWritable_1_, boolean p_ensureWritable_2_)
    {
        return byteBuf.ensureWritable(p_ensureWritable_1_, p_ensureWritable_2_);
    }

    public boolean getBoolean(int p_getBoolean_1_)
    {
        return byteBuf.getBoolean(p_getBoolean_1_);
    }

    public byte getByte(int p_getByte_1_)
    {
        return byteBuf.getByte(p_getByte_1_);
    }

    public short getUnsignedByte(int p_getUnsignedByte_1_)
    {
        return byteBuf.getUnsignedByte(p_getUnsignedByte_1_);
    }

    public short getShort(int p_getShort_1_)
    {
        return byteBuf.getShort(p_getShort_1_);
    }

    public short getShortLE(int p_getShortLE_1_)
    {
        return byteBuf.getShortLE(p_getShortLE_1_);
    }

    public int getUnsignedShort(int p_getUnsignedShort_1_)
    {
        return byteBuf.getUnsignedShort(p_getUnsignedShort_1_);
    }

    public int getUnsignedShortLE(int p_getUnsignedShortLE_1_)
    {
        return byteBuf.getUnsignedShortLE(p_getUnsignedShortLE_1_);
    }

    public int getMedium(int p_getMedium_1_)
    {
        return byteBuf.getMedium(p_getMedium_1_);
    }

    public int getMediumLE(int p_getMediumLE_1_)
    {
        return byteBuf.getMediumLE(p_getMediumLE_1_);
    }

    public int getUnsignedMedium(int p_getUnsignedMedium_1_)
    {
        return byteBuf.getUnsignedMedium(p_getUnsignedMedium_1_);
    }

    public int getUnsignedMediumLE(int p_getUnsignedMediumLE_1_)
    {
        return byteBuf.getUnsignedMediumLE(p_getUnsignedMediumLE_1_);
    }

    public int getInt(int p_getInt_1_)
    {
        return byteBuf.getInt(p_getInt_1_);
    }

    public int getIntLE(int p_getIntLE_1_)
    {
        return byteBuf.getIntLE(p_getIntLE_1_);
    }

    public long getUnsignedInt(int p_getUnsignedInt_1_)
    {
        return byteBuf.getUnsignedInt(p_getUnsignedInt_1_);
    }

    public long getUnsignedIntLE(int p_getUnsignedIntLE_1_)
    {
        return byteBuf.getUnsignedIntLE(p_getUnsignedIntLE_1_);
    }

    public long getLong(int p_getLong_1_)
    {
        return byteBuf.getLong(p_getLong_1_);
    }

    public long getLongLE(int p_getLongLE_1_)
    {
        return byteBuf.getLongLE(p_getLongLE_1_);
    }

    public char getChar(int p_getChar_1_)
    {
        return byteBuf.getChar(p_getChar_1_);
    }

    public float getFloat(int p_getFloat_1_)
    {
        return byteBuf.getFloat(p_getFloat_1_);
    }

    public double getDouble(int p_getDouble_1_)
    {
        return byteBuf.getDouble(p_getDouble_1_);
    }

    public ByteBuf getBytes(int p_getBytes_1_, ByteBuf p_getBytes_2_)
    {
        return byteBuf.getBytes(p_getBytes_1_, p_getBytes_2_);
    }

    public ByteBuf getBytes(int p_getBytes_1_, ByteBuf p_getBytes_2_, int p_getBytes_3_)
    {
        return byteBuf.getBytes(p_getBytes_1_, p_getBytes_2_, p_getBytes_3_);
    }

    public ByteBuf getBytes(int p_getBytes_1_, ByteBuf p_getBytes_2_, int p_getBytes_3_, int p_getBytes_4_)
    {
        return byteBuf.getBytes(p_getBytes_1_, p_getBytes_2_, p_getBytes_3_, p_getBytes_4_);
    }

    public ByteBuf getBytes(int p_getBytes_1_, byte[] p_getBytes_2_)
    {
        return byteBuf.getBytes(p_getBytes_1_, p_getBytes_2_);
    }

    public ByteBuf getBytes(int p_getBytes_1_, byte[] p_getBytes_2_, int p_getBytes_3_, int p_getBytes_4_)
    {
        return byteBuf.getBytes(p_getBytes_1_, p_getBytes_2_, p_getBytes_3_, p_getBytes_4_);
    }

    public ByteBuf getBytes(int p_getBytes_1_, ByteBuffer p_getBytes_2_)
    {
        return byteBuf.getBytes(p_getBytes_1_, p_getBytes_2_);
    }

    public ByteBuf getBytes(int p_getBytes_1_, OutputStream p_getBytes_2_, int p_getBytes_3_) throws IOException
    {
        return byteBuf.getBytes(p_getBytes_1_, p_getBytes_2_, p_getBytes_3_);
    }

    public int getBytes(int p_getBytes_1_, GatheringByteChannel p_getBytes_2_, int p_getBytes_3_) throws IOException
    {
        return byteBuf.getBytes(p_getBytes_1_, p_getBytes_2_, p_getBytes_3_);
    }

    public int getBytes(int p_getBytes_1_, FileChannel p_getBytes_2_, long p_getBytes_3_, int p_getBytes_5_) throws IOException
    {
        return byteBuf.getBytes(p_getBytes_1_, p_getBytes_2_, p_getBytes_3_, p_getBytes_5_);
    }

    public CharSequence getCharSequence(int p_getCharSequence_1_, int p_getCharSequence_2_, Charset p_getCharSequence_3_)
    {
        return byteBuf.getCharSequence(p_getCharSequence_1_, p_getCharSequence_2_, p_getCharSequence_3_);
    }

    public ByteBuf setBoolean(int p_setBoolean_1_, boolean p_setBoolean_2_)
    {
        return byteBuf.setBoolean(p_setBoolean_1_, p_setBoolean_2_);
    }

    public ByteBuf setByte(int p_setByte_1_, int p_setByte_2_)
    {
        return byteBuf.setByte(p_setByte_1_, p_setByte_2_);
    }

    public ByteBuf setShort(int p_setShort_1_, int p_setShort_2_)
    {
        return byteBuf.setShort(p_setShort_1_, p_setShort_2_);
    }

    public ByteBuf setShortLE(int p_setShortLE_1_, int p_setShortLE_2_)
    {
        return byteBuf.setShortLE(p_setShortLE_1_, p_setShortLE_2_);
    }

    public ByteBuf setMedium(int p_setMedium_1_, int p_setMedium_2_)
    {
        return byteBuf.setMedium(p_setMedium_1_, p_setMedium_2_);
    }

    public ByteBuf setMediumLE(int p_setMediumLE_1_, int p_setMediumLE_2_)
    {
        return byteBuf.setMediumLE(p_setMediumLE_1_, p_setMediumLE_2_);
    }

    public ByteBuf setInt(int p_setInt_1_, int p_setInt_2_)
    {
        return byteBuf.setInt(p_setInt_1_, p_setInt_2_);
    }

    public ByteBuf setIntLE(int p_setIntLE_1_, int p_setIntLE_2_)
    {
        return byteBuf.setIntLE(p_setIntLE_1_, p_setIntLE_2_);
    }

    public ByteBuf setLong(int p_setLong_1_, long p_setLong_2_)
    {
        return byteBuf.setLong(p_setLong_1_, p_setLong_2_);
    }

    public ByteBuf setLongLE(int p_setLongLE_1_, long p_setLongLE_2_)
    {
        return byteBuf.setLongLE(p_setLongLE_1_, p_setLongLE_2_);
    }

    public ByteBuf setChar(int p_setChar_1_, int p_setChar_2_)
    {
        return byteBuf.setChar(p_setChar_1_, p_setChar_2_);
    }

    public ByteBuf setFloat(int p_setFloat_1_, float p_setFloat_2_)
    {
        return byteBuf.setFloat(p_setFloat_1_, p_setFloat_2_);
    }

    public ByteBuf setDouble(int p_setDouble_1_, double p_setDouble_2_)
    {
        return byteBuf.setDouble(p_setDouble_1_, p_setDouble_2_);
    }

    public ByteBuf setBytes(int p_setBytes_1_, ByteBuf p_setBytes_2_)
    {
        return byteBuf.setBytes(p_setBytes_1_, p_setBytes_2_);
    }

    public ByteBuf setBytes(int p_setBytes_1_, ByteBuf p_setBytes_2_, int p_setBytes_3_)
    {
        return byteBuf.setBytes(p_setBytes_1_, p_setBytes_2_, p_setBytes_3_);
    }

    public ByteBuf setBytes(int p_setBytes_1_, ByteBuf p_setBytes_2_, int p_setBytes_3_, int p_setBytes_4_)
    {
        return byteBuf.setBytes(p_setBytes_1_, p_setBytes_2_, p_setBytes_3_, p_setBytes_4_);
    }

    public ByteBuf setBytes(int p_setBytes_1_, byte[] p_setBytes_2_)
    {
        return byteBuf.setBytes(p_setBytes_1_, p_setBytes_2_);
    }

    public ByteBuf setBytes(int p_setBytes_1_, byte[] p_setBytes_2_, int p_setBytes_3_, int p_setBytes_4_)
    {
        return byteBuf.setBytes(p_setBytes_1_, p_setBytes_2_, p_setBytes_3_, p_setBytes_4_);
    }

    public ByteBuf setBytes(int p_setBytes_1_, ByteBuffer p_setBytes_2_)
    {
        return byteBuf.setBytes(p_setBytes_1_, p_setBytes_2_);
    }

    public int setBytes(int p_setBytes_1_, InputStream p_setBytes_2_, int p_setBytes_3_) throws IOException
    {
        return byteBuf.setBytes(p_setBytes_1_, p_setBytes_2_, p_setBytes_3_);
    }

    public int setBytes(int p_setBytes_1_, ScatteringByteChannel p_setBytes_2_, int p_setBytes_3_) throws IOException
    {
        return byteBuf.setBytes(p_setBytes_1_, p_setBytes_2_, p_setBytes_3_);
    }

    public int setBytes(int p_setBytes_1_, FileChannel p_setBytes_2_, long p_setBytes_3_, int p_setBytes_5_) throws IOException
    {
        return byteBuf.setBytes(p_setBytes_1_, p_setBytes_2_, p_setBytes_3_, p_setBytes_5_);
    }

    public ByteBuf setZero(int p_setZero_1_, int p_setZero_2_)
    {
        return byteBuf.setZero(p_setZero_1_, p_setZero_2_);
    }

    public int setCharSequence(int p_setCharSequence_1_, CharSequence p_setCharSequence_2_, Charset p_setCharSequence_3_)
    {
        return byteBuf.setCharSequence(p_setCharSequence_1_, p_setCharSequence_2_, p_setCharSequence_3_);
    }

    public boolean readBoolean()
    {
        return byteBuf.readBoolean();
    }

    public byte readByte()
    {
        return byteBuf.readByte();
    }

    public short readUnsignedByte()
    {
        return byteBuf.readUnsignedByte();
    }

    public short readShort()
    {
        return byteBuf.readShort();
    }

    public short readShortLE()
    {
        return byteBuf.readShortLE();
    }

    public int readUnsignedShort()
    {
        return byteBuf.readUnsignedShort();
    }

    public int readUnsignedShortLE()
    {
        return byteBuf.readUnsignedShortLE();
    }

    public int readMedium()
    {
        return byteBuf.readMedium();
    }

    public int readMediumLE()
    {
        return byteBuf.readMediumLE();
    }

    public int readUnsignedMedium()
    {
        return byteBuf.readUnsignedMedium();
    }

    public int readUnsignedMediumLE()
    {
        return byteBuf.readUnsignedMediumLE();
    }

    public int readInt()
    {
        return byteBuf.readInt();
    }

    public int readIntLE()
    {
        return byteBuf.readIntLE();
    }

    public long readUnsignedInt()
    {
        return byteBuf.readUnsignedInt();
    }

    public long readUnsignedIntLE()
    {
        return byteBuf.readUnsignedIntLE();
    }

    public long readLong()
    {
        return byteBuf.readLong();
    }

    public long readLongLE()
    {
        return byteBuf.readLongLE();
    }

    public char readChar()
    {
        return byteBuf.readChar();
    }

    public float readFloat()
    {
        return byteBuf.readFloat();
    }

    public double readDouble()
    {
        return byteBuf.readDouble();
    }

    public ByteBuf readBytes(int p_readBytes_1_)
    {
        return byteBuf.readBytes(p_readBytes_1_);
    }

    public ByteBuf readSlice(int p_readSlice_1_)
    {
        return byteBuf.readSlice(p_readSlice_1_);
    }

    public ByteBuf readRetainedSlice(int p_readRetainedSlice_1_)
    {
        return byteBuf.readRetainedSlice(p_readRetainedSlice_1_);
    }

    public ByteBuf readBytes(ByteBuf p_readBytes_1_)
    {
        return byteBuf.readBytes(p_readBytes_1_);
    }

    public ByteBuf readBytes(ByteBuf p_readBytes_1_, int p_readBytes_2_)
    {
        return byteBuf.readBytes(p_readBytes_1_, p_readBytes_2_);
    }

    public ByteBuf readBytes(ByteBuf p_readBytes_1_, int p_readBytes_2_, int p_readBytes_3_)
    {
        return byteBuf.readBytes(p_readBytes_1_, p_readBytes_2_, p_readBytes_3_);
    }

    public ByteBuf readBytes(byte[] p_readBytes_1_)
    {
        return byteBuf.readBytes(p_readBytes_1_);
    }

    public ByteBuf readBytes(byte[] p_readBytes_1_, int p_readBytes_2_, int p_readBytes_3_)
    {
        return byteBuf.readBytes(p_readBytes_1_, p_readBytes_2_, p_readBytes_3_);
    }

    public ByteBuf readBytes(ByteBuffer p_readBytes_1_)
    {
        return byteBuf.readBytes(p_readBytes_1_);
    }

    public ByteBuf readBytes(OutputStream p_readBytes_1_, int p_readBytes_2_) throws IOException
    {
        return byteBuf.readBytes(p_readBytes_1_, p_readBytes_2_);
    }

    public int readBytes(GatheringByteChannel p_readBytes_1_, int p_readBytes_2_) throws IOException
    {
        return byteBuf.readBytes(p_readBytes_1_, p_readBytes_2_);
    }

    public CharSequence readCharSequence(int p_readCharSequence_1_, Charset p_readCharSequence_2_)
    {
        return byteBuf.readCharSequence(p_readCharSequence_1_, p_readCharSequence_2_);
    }

    public int readBytes(FileChannel p_readBytes_1_, long p_readBytes_2_, int p_readBytes_4_) throws IOException
    {
        return byteBuf.readBytes(p_readBytes_1_, p_readBytes_2_, p_readBytes_4_);
    }

    public ByteBuf skipBytes(int p_skipBytes_1_)
    {
        return byteBuf.skipBytes(p_skipBytes_1_);
    }

    public ByteBuf writeBoolean(boolean p_writeBoolean_1_)
    {
        return byteBuf.writeBoolean(p_writeBoolean_1_);
    }

    public ByteBuf writeByte(int p_writeByte_1_)
    {
        return byteBuf.writeByte(p_writeByte_1_);
    }

    public ByteBuf writeShort(int p_writeShort_1_)
    {
        return byteBuf.writeShort(p_writeShort_1_);
    }

    public ByteBuf writeShortLE(int p_writeShortLE_1_)
    {
        return byteBuf.writeShortLE(p_writeShortLE_1_);
    }

    public ByteBuf writeMedium(int p_writeMedium_1_)
    {
        return byteBuf.writeMedium(p_writeMedium_1_);
    }

    public ByteBuf writeMediumLE(int p_writeMediumLE_1_)
    {
        return byteBuf.writeMediumLE(p_writeMediumLE_1_);
    }

    public ByteBuf writeInt(int p_writeInt_1_)
    {
        return byteBuf.writeInt(p_writeInt_1_);
    }

    public ByteBuf writeIntLE(int p_writeIntLE_1_)
    {
        return byteBuf.writeIntLE(p_writeIntLE_1_);
    }

    public ByteBuf writeLong(long p_writeLong_1_)
    {
        return byteBuf.writeLong(p_writeLong_1_);
    }

    public ByteBuf writeLongLE(long p_writeLongLE_1_)
    {
        return byteBuf.writeLongLE(p_writeLongLE_1_);
    }

    public ByteBuf writeChar(int p_writeChar_1_)
    {
        return byteBuf.writeChar(p_writeChar_1_);
    }

    public ByteBuf writeFloat(float p_writeFloat_1_)
    {
        return byteBuf.writeFloat(p_writeFloat_1_);
    }

    public ByteBuf writeDouble(double p_writeDouble_1_)
    {
        return byteBuf.writeDouble(p_writeDouble_1_);
    }

    public ByteBuf writeBytes(ByteBuf p_writeBytes_1_)
    {
        return byteBuf.writeBytes(p_writeBytes_1_);
    }

    public ByteBuf writeBytes(ByteBuf p_writeBytes_1_, int p_writeBytes_2_)
    {
        return byteBuf.writeBytes(p_writeBytes_1_, p_writeBytes_2_);
    }

    public ByteBuf writeBytes(ByteBuf p_writeBytes_1_, int p_writeBytes_2_, int p_writeBytes_3_)
    {
        return byteBuf.writeBytes(p_writeBytes_1_, p_writeBytes_2_, p_writeBytes_3_);
    }

    public ByteBuf writeBytes(byte[] p_writeBytes_1_)
    {
        return byteBuf.writeBytes(p_writeBytes_1_);
    }

    public ByteBuf writeBytes(byte[] p_writeBytes_1_, int p_writeBytes_2_, int p_writeBytes_3_)
    {
        return byteBuf.writeBytes(p_writeBytes_1_, p_writeBytes_2_, p_writeBytes_3_);
    }

    public ByteBuf writeBytes(ByteBuffer p_writeBytes_1_)
    {
        return byteBuf.writeBytes(p_writeBytes_1_);
    }

    public int writeBytes(InputStream p_writeBytes_1_, int p_writeBytes_2_) throws IOException
    {
        return byteBuf.writeBytes(p_writeBytes_1_, p_writeBytes_2_);
    }

    public int writeBytes(ScatteringByteChannel p_writeBytes_1_, int p_writeBytes_2_) throws IOException
    {
        return byteBuf.writeBytes(p_writeBytes_1_, p_writeBytes_2_);
    }

    public int writeBytes(FileChannel p_writeBytes_1_, long p_writeBytes_2_, int p_writeBytes_4_) throws IOException
    {
        return byteBuf.writeBytes(p_writeBytes_1_, p_writeBytes_2_, p_writeBytes_4_);
    }

    public ByteBuf writeZero(int p_writeZero_1_)
    {
        return byteBuf.writeZero(p_writeZero_1_);
    }

    public int writeCharSequence(CharSequence p_writeCharSequence_1_, Charset p_writeCharSequence_2_)
    {
        return byteBuf.writeCharSequence(p_writeCharSequence_1_, p_writeCharSequence_2_);
    }

    public int indexOf(int p_indexOf_1_, int p_indexOf_2_, byte p_indexOf_3_)
    {
        return byteBuf.indexOf(p_indexOf_1_, p_indexOf_2_, p_indexOf_3_);
    }

    public int bytesBefore(byte p_bytesBefore_1_)
    {
        return byteBuf.bytesBefore(p_bytesBefore_1_);
    }

    public int bytesBefore(int p_bytesBefore_1_, byte p_bytesBefore_2_)
    {
        return byteBuf.bytesBefore(p_bytesBefore_1_, p_bytesBefore_2_);
    }

    public int bytesBefore(int p_bytesBefore_1_, int p_bytesBefore_2_, byte p_bytesBefore_3_)
    {
        return byteBuf.bytesBefore(p_bytesBefore_1_, p_bytesBefore_2_, p_bytesBefore_3_);
    }

    public int forEachByte(ByteProcessor p_forEachByte_1_)
    {
        return byteBuf.forEachByte(p_forEachByte_1_);
    }

    public int forEachByte(int p_forEachByte_1_, int p_forEachByte_2_, ByteProcessor p_forEachByte_3_)
    {
        return byteBuf.forEachByte(p_forEachByte_1_, p_forEachByte_2_, p_forEachByte_3_);
    }

    public int forEachByteDesc(ByteProcessor p_forEachByteDesc_1_)
    {
        return byteBuf.forEachByteDesc(p_forEachByteDesc_1_);
    }

    public int forEachByteDesc(int p_forEachByteDesc_1_, int p_forEachByteDesc_2_, ByteProcessor p_forEachByteDesc_3_)
    {
        return byteBuf.forEachByteDesc(p_forEachByteDesc_1_, p_forEachByteDesc_2_, p_forEachByteDesc_3_);
    }

    public ByteBuf copy()
    {
        return byteBuf.copy();
    }

    public ByteBuf copy(int p_copy_1_, int p_copy_2_)
    {
        return byteBuf.copy(p_copy_1_, p_copy_2_);
    }

    public ByteBuf slice()
    {
        return byteBuf.slice();
    }

    public ByteBuf retainedSlice()
    {
        return byteBuf.retainedSlice();
    }

    public ByteBuf slice(int p_slice_1_, int p_slice_2_)
    {
        return byteBuf.slice(p_slice_1_, p_slice_2_);
    }

    public ByteBuf retainedSlice(int p_retainedSlice_1_, int p_retainedSlice_2_)
    {
        return byteBuf.retainedSlice(p_retainedSlice_1_, p_retainedSlice_2_);
    }

    public ByteBuf duplicate()
    {
        return byteBuf.duplicate();
    }

    public ByteBuf retainedDuplicate()
    {
        return byteBuf.retainedDuplicate();
    }

    public int nioBufferCount()
    {
        return byteBuf.nioBufferCount();
    }

    public ByteBuffer nioBuffer()
    {
        return byteBuf.nioBuffer();
    }

    public ByteBuffer nioBuffer(int p_nioBuffer_1_, int p_nioBuffer_2_)
    {
        return byteBuf.nioBuffer(p_nioBuffer_1_, p_nioBuffer_2_);
    }

    public ByteBuffer internalNioBuffer(int p_internalNioBuffer_1_, int p_internalNioBuffer_2_)
    {
        return byteBuf.internalNioBuffer(p_internalNioBuffer_1_, p_internalNioBuffer_2_);
    }

    public ByteBuffer[] nioBuffers()
    {
        return byteBuf.nioBuffers();
    }

    public ByteBuffer[] nioBuffers(int p_nioBuffers_1_, int p_nioBuffers_2_)
    {
        return byteBuf.nioBuffers(p_nioBuffers_1_, p_nioBuffers_2_);
    }

    public boolean hasArray()
    {
        return byteBuf.hasArray();
    }

    public byte[] array()
    {
        return byteBuf.array();
    }

    public int arrayOffset()
    {
        return byteBuf.arrayOffset();
    }

    public boolean hasMemoryAddress()
    {
        return byteBuf.hasMemoryAddress();
    }

    public long memoryAddress()
    {
        return byteBuf.memoryAddress();
    }

    public String toString(Charset p_toString_1_)
    {
        return byteBuf.toString(p_toString_1_);
    }

    public String toString(int p_toString_1_, int p_toString_2_, Charset p_toString_3_)
    {
        return byteBuf.toString(p_toString_1_, p_toString_2_, p_toString_3_);
    }

    public int hashCode()
    {
        return byteBuf.hashCode();
    }

    public boolean equals(Object p_equals_1_) {
        return byteBuf.equals(p_equals_1_);
    }

    public int compareTo(ByteBuf p_compareTo_1_) {
        return byteBuf.compareTo(p_compareTo_1_);
    }

    public String toString() {
        return byteBuf.toString();
    }

    public ByteBuf retain(int p_retain_1_) {
        return byteBuf.retain(p_retain_1_);
    }

    public ByteBuf retain() {
        return byteBuf.retain();
    }

    public ByteBuf touch() {
        return byteBuf.touch();
    }

    public ByteBuf touch(Object p_touch_1_) {
        return byteBuf.touch(p_touch_1_);
    }

    public int refCnt() {
        return byteBuf.refCnt();
    }

    public boolean release() {
        return byteBuf.release();
    }

    public boolean release(int p_release_1_) {
        return byteBuf.release(p_release_1_);
    }
}