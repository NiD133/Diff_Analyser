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

public class Base16TestTest30 {

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
    void testStringToByteVariations() throws DecoderException {
        final Base16 base16 = new Base16();
        final String s1 = "48656C6C6F20576F726C64";
        final String s2 = "";
        final String s3 = null;
        assertEquals("Hello World", StringUtils.newStringUtf8(base16.decode(s1)), "StringToByte Hello World");
        assertEquals("Hello World", StringUtils.newStringUtf8((byte[]) new Base16().decode((Object) s1)), "StringToByte Hello World");
        assertEquals("Hello World", StringUtils.newStringUtf8(new Base16().decode(s1)), "StringToByte static Hello World");
        assertEquals("", StringUtils.newStringUtf8(new Base16().decode(s2)), "StringToByte \"\"");
        assertEquals("", StringUtils.newStringUtf8(new Base16().decode(s2)), "StringToByte static \"\"");
        assertNull(StringUtils.newStringUtf8(new Base16().decode(s3)), "StringToByte null");
        assertNull(StringUtils.newStringUtf8(new Base16().decode(s3)), "StringToByte static null");
    }
}
