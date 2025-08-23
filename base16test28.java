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

public class Base16TestTest28 {

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
    void testSingletons() {
        assertEquals("00", new String(new Base16().encode(new byte[] { (byte) 0 })));
        assertEquals("01", new String(new Base16().encode(new byte[] { (byte) 1 })));
        assertEquals("02", new String(new Base16().encode(new byte[] { (byte) 2 })));
        assertEquals("03", new String(new Base16().encode(new byte[] { (byte) 3 })));
        assertEquals("04", new String(new Base16().encode(new byte[] { (byte) 4 })));
        assertEquals("05", new String(new Base16().encode(new byte[] { (byte) 5 })));
        assertEquals("06", new String(new Base16().encode(new byte[] { (byte) 6 })));
        assertEquals("07", new String(new Base16().encode(new byte[] { (byte) 7 })));
        assertEquals("08", new String(new Base16().encode(new byte[] { (byte) 8 })));
        assertEquals("09", new String(new Base16().encode(new byte[] { (byte) 9 })));
        assertEquals("0A", new String(new Base16().encode(new byte[] { (byte) 10 })));
        assertEquals("0B", new String(new Base16().encode(new byte[] { (byte) 11 })));
        assertEquals("0C", new String(new Base16().encode(new byte[] { (byte) 12 })));
        assertEquals("0D", new String(new Base16().encode(new byte[] { (byte) 13 })));
        assertEquals("0E", new String(new Base16().encode(new byte[] { (byte) 14 })));
        assertEquals("0F", new String(new Base16().encode(new byte[] { (byte) 15 })));
        assertEquals("10", new String(new Base16().encode(new byte[] { (byte) 16 })));
        assertEquals("11", new String(new Base16().encode(new byte[] { (byte) 17 })));
        assertEquals("12", new String(new Base16().encode(new byte[] { (byte) 18 })));
        assertEquals("13", new String(new Base16().encode(new byte[] { (byte) 19 })));
        assertEquals("14", new String(new Base16().encode(new byte[] { (byte) 20 })));
        assertEquals("15", new String(new Base16().encode(new byte[] { (byte) 21 })));
        assertEquals("16", new String(new Base16().encode(new byte[] { (byte) 22 })));
        assertEquals("17", new String(new Base16().encode(new byte[] { (byte) 23 })));
        assertEquals("18", new String(new Base16().encode(new byte[] { (byte) 24 })));
        assertEquals("19", new String(new Base16().encode(new byte[] { (byte) 25 })));
        assertEquals("1A", new String(new Base16().encode(new byte[] { (byte) 26 })));
        assertEquals("1B", new String(new Base16().encode(new byte[] { (byte) 27 })));
        assertEquals("1C", new String(new Base16().encode(new byte[] { (byte) 28 })));
        assertEquals("1D", new String(new Base16().encode(new byte[] { (byte) 29 })));
        assertEquals("1E", new String(new Base16().encode(new byte[] { (byte) 30 })));
        assertEquals("1F", new String(new Base16().encode(new byte[] { (byte) 31 })));
        assertEquals("20", new String(new Base16().encode(new byte[] { (byte) 32 })));
        assertEquals("21", new String(new Base16().encode(new byte[] { (byte) 33 })));
        assertEquals("22", new String(new Base16().encode(new byte[] { (byte) 34 })));
        assertEquals("23", new String(new Base16().encode(new byte[] { (byte) 35 })));
        assertEquals("24", new String(new Base16().encode(new byte[] { (byte) 36 })));
        assertEquals("25", new String(new Base16().encode(new byte[] { (byte) 37 })));
        assertEquals("26", new String(new Base16().encode(new byte[] { (byte) 38 })));
        assertEquals("27", new String(new Base16().encode(new byte[] { (byte) 39 })));
        assertEquals("28", new String(new Base16().encode(new byte[] { (byte) 40 })));
        assertEquals("29", new String(new Base16().encode(new byte[] { (byte) 41 })));
        assertEquals("2A", new String(new Base16().encode(new byte[] { (byte) 42 })));
        assertEquals("2B", new String(new Base16().encode(new byte[] { (byte) 43 })));
        assertEquals("2C", new String(new Base16().encode(new byte[] { (byte) 44 })));
        assertEquals("2D", new String(new Base16().encode(new byte[] { (byte) 45 })));
        assertEquals("2E", new String(new Base16().encode(new byte[] { (byte) 46 })));
        assertEquals("2F", new String(new Base16().encode(new byte[] { (byte) 47 })));
        assertEquals("30", new String(new Base16().encode(new byte[] { (byte) 48 })));
        assertEquals("31", new String(new Base16().encode(new byte[] { (byte) 49 })));
        assertEquals("32", new String(new Base16().encode(new byte[] { (byte) 50 })));
        assertEquals("33", new String(new Base16().encode(new byte[] { (byte) 51 })));
        assertEquals("34", new String(new Base16().encode(new byte[] { (byte) 52 })));
        assertEquals("35", new String(new Base16().encode(new byte[] { (byte) 53 })));
        assertEquals("36", new String(new Base16().encode(new byte[] { (byte) 54 })));
        assertEquals("37", new String(new Base16().encode(new byte[] { (byte) 55 })));
        assertEquals("38", new String(new Base16().encode(new byte[] { (byte) 56 })));
        assertEquals("39", new String(new Base16().encode(new byte[] { (byte) 57 })));
        assertEquals("3A", new String(new Base16().encode(new byte[] { (byte) 58 })));
        assertEquals("3B", new String(new Base16().encode(new byte[] { (byte) 59 })));
        assertEquals("3C", new String(new Base16().encode(new byte[] { (byte) 60 })));
        assertEquals("3D", new String(new Base16().encode(new byte[] { (byte) 61 })));
        assertEquals("3E", new String(new Base16().encode(new byte[] { (byte) 62 })));
        assertEquals("3F", new String(new Base16().encode(new byte[] { (byte) 63 })));
        assertEquals("40", new String(new Base16().encode(new byte[] { (byte) 64 })));
        assertEquals("41", new String(new Base16().encode(new byte[] { (byte) 65 })));
        assertEquals("42", new String(new Base16().encode(new byte[] { (byte) 66 })));
        assertEquals("43", new String(new Base16().encode(new byte[] { (byte) 67 })));
        assertEquals("44", new String(new Base16().encode(new byte[] { (byte) 68 })));
        assertEquals("45", new String(new Base16().encode(new byte[] { (byte) 69 })));
        assertEquals("46", new String(new Base16().encode(new byte[] { (byte) 70 })));
        assertEquals("47", new String(new Base16().encode(new byte[] { (byte) 71 })));
        assertEquals("48", new String(new Base16().encode(new byte[] { (byte) 72 })));
        assertEquals("49", new String(new Base16().encode(new byte[] { (byte) 73 })));
        assertEquals("4A", new String(new Base16().encode(new byte[] { (byte) 74 })));
        assertEquals("4B", new String(new Base16().encode(new byte[] { (byte) 75 })));
        assertEquals("4C", new String(new Base16().encode(new byte[] { (byte) 76 })));
        assertEquals("4D", new String(new Base16().encode(new byte[] { (byte) 77 })));
        assertEquals("4E", new String(new Base16().encode(new byte[] { (byte) 78 })));
        assertEquals("4F", new String(new Base16().encode(new byte[] { (byte) 79 })));
        assertEquals("50", new String(new Base16().encode(new byte[] { (byte) 80 })));
        assertEquals("51", new String(new Base16().encode(new byte[] { (byte) 81 })));
        assertEquals("52", new String(new Base16().encode(new byte[] { (byte) 82 })));
        assertEquals("53", new String(new Base16().encode(new byte[] { (byte) 83 })));
        assertEquals("54", new String(new Base16().encode(new byte[] { (byte) 84 })));
        assertEquals("55", new String(new Base16().encode(new byte[] { (byte) 85 })));
        assertEquals("56", new String(new Base16().encode(new byte[] { (byte) 86 })));
        assertEquals("57", new String(new Base16().encode(new byte[] { (byte) 87 })));
        assertEquals("58", new String(new Base16().encode(new byte[] { (byte) 88 })));
        assertEquals("59", new String(new Base16().encode(new byte[] { (byte) 89 })));
        assertEquals("5A", new String(new Base16().encode(new byte[] { (byte) 90 })));
        assertEquals("5B", new String(new Base16().encode(new byte[] { (byte) 91 })));
        assertEquals("5C", new String(new Base16().encode(new byte[] { (byte) 92 })));
        assertEquals("5D", new String(new Base16().encode(new byte[] { (byte) 93 })));
        assertEquals("5E", new String(new Base16().encode(new byte[] { (byte) 94 })));
        assertEquals("5F", new String(new Base16().encode(new byte[] { (byte) 95 })));
        assertEquals("60", new String(new Base16().encode(new byte[] { (byte) 96 })));
        assertEquals("61", new String(new Base16().encode(new byte[] { (byte) 97 })));
        assertEquals("62", new String(new Base16().encode(new byte[] { (byte) 98 })));
        assertEquals("63", new String(new Base16().encode(new byte[] { (byte) 99 })));
        assertEquals("64", new String(new Base16().encode(new byte[] { (byte) 100 })));
        assertEquals("65", new String(new Base16().encode(new byte[] { (byte) 101 })));
        assertEquals("66", new String(new Base16().encode(new byte[] { (byte) 102 })));
        assertEquals("67", new String(new Base16().encode(new byte[] { (byte) 103 })));
        assertEquals("68", new String(new Base16().encode(new byte[] { (byte) 104 })));
        for (int i = -128; i <= 127; i++) {
            final byte[] test = { (byte) i };
            assertArrayEquals(test, new Base16().decode(new Base16().encode(test)));
        }
    }
}
