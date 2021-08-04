package com.ywrain.cache.redis.configs;

import java.nio.ByteBuffer;
import java.nio.charset.CharsetEncoder;

import com.lambdaworks.redis.codec.RedisCodec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

/**
 * 自定义的Codec解析
 *
 * @author xuguangming@ywrain.com
 * @date create in 2019/3/14
 */
public class CustomByteArrayCodec implements RedisCodec<String, byte[]> {

    public static final CustomByteArrayCodec INSTANCE = new CustomByteArrayCodec();
    private static final byte[] EMPTY = new byte[0];

    @Override
    public String decodeKey(ByteBuffer bytes) {
        return Unpooled.wrappedBuffer(bytes).toString(RedisConfig.UTF8);
    }

    @Override
    public byte[] decodeValue(ByteBuffer bytes) {
        return getBytes(bytes);
    }

    @Override
    public ByteBuffer encodeKey(String key) {
        return encodeAndAllocateBuffer(key);
    }

    @Override
    public ByteBuffer encodeValue(byte[] value) {
        if (value == null) {
            return ByteBuffer.wrap(EMPTY);
        }

        return ByteBuffer.wrap(value);
    }

    /**
     * Compatibility implementation.
     */
    private ByteBuffer encodeAndAllocateBuffer(String key) {
        if (key == null) {
            return ByteBuffer.wrap(EMPTY);
        }

        CharsetEncoder encoder = CharsetUtil.encoder(RedisConfig.UTF8);
        ByteBuffer buffer = ByteBuffer.allocate((int) (encoder.maxBytesPerChar() * key.length()));

        ByteBuf byteBuf = Unpooled.wrappedBuffer(buffer);
        byteBuf.clear();
        encode(key, byteBuf);
        buffer.limit(byteBuf.writerIndex());

        return buffer;
    }

    public void encode(String str, ByteBuf target) {
        if (str == null) {
            return;
        }

        ByteBufUtil.writeUtf8(target, str);
    }

    private static byte[] getBytes(ByteBuffer buffer) {
        int remaining = buffer.remaining();

        if (remaining == 0) {
            return EMPTY;
        }

        byte[] b = new byte[remaining];
        buffer.get(b);
        return b;
    }
}
