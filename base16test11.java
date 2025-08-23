package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import org.apache.commons.codec.CodecPolicy;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

public class Base16TestTest11 {

    private static final Charset CHARSET_UTF8 = StandardCharsets.UTF_8;

    private final Random random = new Random();

    /**
     * @return the random.
     */
    public Random getRandom() {
        return this.random;
    }

    private void testBase16InBuffer(final int startPasSize, final int endPadSize) {
        final String content = "Hello World";
        final String encodedContent;
        final byte[] bytesUtf8 = StringUtils.getBytesUtf8(content);
        byte[] buffer = ArrayUtils.addAll(bytesUtf8, new byte[endPadSize]);
        buffer = ArrayUtils.addAll(new byte[startPasSize], buffer);
        final byte[] encodedBytes = new Base16().encode(buffer, startPasSize, bytesUtf8.length);
        encodedContent = StringUtils.newStringUtf8(encodedBytes);
        assertEquals("48656C6C6F20576F726C64", encodedContent, "encoding hello world");
    }

    private String toString(final byte[] data) {
        final StringBuilder buf = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            buf.append(data[i]);
            if (i != data.length - 1) {
                buf.append(",");
            }
        }
        return buf.toString();
    }

    @Test
    void testDecodeSingleBytes() {
        final String encoded = "556E74696C206E6578742074696D6521";
        final BaseNCodec.Context context = new BaseNCodec.Context();
        final Base16 b16 = new Base16();
        final byte[] encocdedBytes = StringUtils.getBytesUtf8(encoded);
        // decode byte-by-byte
        b16.decode(encocdedBytes, 0, 1, context);
        // yields "U"
        b16.decode(encocdedBytes, 1, 1, context);
        b16.decode(encocdedBytes, 2, 1, context);
        // yields "n"
        b16.decode(encocdedBytes, 3, 1, context);
        // decode split hex-pairs
        // yields "t"
        b16.decode(encocdedBytes, 4, 3, context);
        // yields "il"
        b16.decode(encocdedBytes, 7, 3, context);
        // yields " "
        b16.decode(encocdedBytes, 10, 3, context);
        // decode remaining
        // yields "next time!"
        b16.decode(encocdedBytes, 13, 19, context);
        final byte[] decodedBytes = new byte[context.pos];
        System.arraycopy(context.buffer, context.readPos, decodedBytes, 0, decodedBytes.length);
        final String decoded = StringUtils.newStringUtf8(decodedBytes);
        assertEquals("Until next time!", decoded);
    }
}
