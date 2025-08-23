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

public class Base16TestTest16 {

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
    void testIsInAlphabet() {
        // invalid bounds
        Base16 b16 = new Base16(true);
        assertFalse(b16.isInAlphabet((byte) 0));
        assertFalse(b16.isInAlphabet((byte) 1));
        assertFalse(b16.isInAlphabet((byte) -1));
        assertFalse(b16.isInAlphabet((byte) -15));
        assertFalse(b16.isInAlphabet((byte) -16));
        assertFalse(b16.isInAlphabet((byte) 128));
        assertFalse(b16.isInAlphabet((byte) 255));
        // lower-case
        b16 = new Base16(true);
        for (char c = '0'; c <= '9'; c++) {
            assertTrue(b16.isInAlphabet((byte) c));
        }
        for (char c = 'a'; c <= 'f'; c++) {
            assertTrue(b16.isInAlphabet((byte) c));
        }
        for (char c = 'A'; c <= 'F'; c++) {
            assertFalse(b16.isInAlphabet((byte) c));
        }
        assertFalse(b16.isInAlphabet((byte) ('0' - 1)));
        assertFalse(b16.isInAlphabet((byte) ('9' + 1)));
        assertFalse(b16.isInAlphabet((byte) ('a' - 1)));
        assertFalse(b16.isInAlphabet((byte) ('f' + 1)));
        assertFalse(b16.isInAlphabet((byte) ('z' + 1)));
        // upper-case
        b16 = new Base16(false);
        for (char c = '0'; c <= '9'; c++) {
            assertTrue(b16.isInAlphabet((byte) c));
        }
        for (char c = 'a'; c <= 'f'; c++) {
            assertFalse(b16.isInAlphabet((byte) c));
        }
        for (char c = 'A'; c <= 'F'; c++) {
            assertTrue(b16.isInAlphabet((byte) c));
        }
        assertFalse(b16.isInAlphabet((byte) ('0' - 1)));
        assertFalse(b16.isInAlphabet((byte) ('9' + 1)));
        assertFalse(b16.isInAlphabet((byte) ('A' - 1)));
        assertFalse(b16.isInAlphabet((byte) ('F' + 1)));
        assertFalse(b16.isInAlphabet((byte) ('Z' + 1)));
    }
}
