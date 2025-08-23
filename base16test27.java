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

public class Base16TestTest27 {

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
    void testPairs() {
        assertEquals("0000", new String(new Base16().encode(new byte[] { (byte) 0, (byte) 0 })));
        assertEquals("0001", new String(new Base16().encode(new byte[] { (byte) 0, (byte) 1 })));
        assertEquals("0002", new String(new Base16().encode(new byte[] { (byte) 0, (byte) 2 })));
        assertEquals("0003", new String(new Base16().encode(new byte[] { (byte) 0, (byte) 3 })));
        assertEquals("0004", new String(new Base16().encode(new byte[] { (byte) 0, (byte) 4 })));
        assertEquals("0005", new String(new Base16().encode(new byte[] { (byte) 0, (byte) 5 })));
        assertEquals("0006", new String(new Base16().encode(new byte[] { (byte) 0, (byte) 6 })));
        assertEquals("0007", new String(new Base16().encode(new byte[] { (byte) 0, (byte) 7 })));
        assertEquals("0008", new String(new Base16().encode(new byte[] { (byte) 0, (byte) 8 })));
        assertEquals("0009", new String(new Base16().encode(new byte[] { (byte) 0, (byte) 9 })));
        assertEquals("000A", new String(new Base16().encode(new byte[] { (byte) 0, (byte) 10 })));
        assertEquals("000B", new String(new Base16().encode(new byte[] { (byte) 0, (byte) 11 })));
        assertEquals("000C", new String(new Base16().encode(new byte[] { (byte) 0, (byte) 12 })));
        assertEquals("000D", new String(new Base16().encode(new byte[] { (byte) 0, (byte) 13 })));
        assertEquals("000E", new String(new Base16().encode(new byte[] { (byte) 0, (byte) 14 })));
        assertEquals("000F", new String(new Base16().encode(new byte[] { (byte) 0, (byte) 15 })));
        assertEquals("0010", new String(new Base16().encode(new byte[] { (byte) 0, (byte) 16 })));
        assertEquals("0011", new String(new Base16().encode(new byte[] { (byte) 0, (byte) 17 })));
        for (int i = -128; i <= 127; i++) {
            final byte[] test = { (byte) i, (byte) i };
            assertArrayEquals(test, new Base16().decode(new Base16().encode(test)));
        }
    }
}
