package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.BitSet;

public class QuotedPrintableCodecTest {

    private static final Charset DEFAULT_CHARSET = Charset.defaultCharset();
    private static final String UTF_8 = "UTF-8";
    private static final byte ESCAPE_CHAR = '=';
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    @Test
    public void testEncodeQuotedPrintableWithEmptyByteArray() {
        BitSet bitSet = new BitSet();
        byte[] encoded = QuotedPrintableCodec.encodeQuotedPrintable(bitSet, EMPTY_BYTE_ARRAY, false);
        assertArrayEquals(EMPTY_BYTE_ARRAY, encoded);
    }

    @Test
    public void testEncodeQuotedPrintableWithNullBitSet() {
        byte[] byteArray = new byte[8];
        byte[] encoded = QuotedPrintableCodec.encodeQuotedPrintable(null, byteArray, true);
        assertEquals(8, encoded.length);
        assertArrayEquals(new byte[] {0, 0, 0, 0, 0, 0, 0, 0}, encoded);
    }

    @Test
    public void testDecodeQuotedPrintableWithEmptyByteArray() throws Throwable {
        byte[] decoded = QuotedPrintableCodec.decodeQuotedPrintable(EMPTY_BYTE_ARRAY);
        assertArrayEquals(EMPTY_BYTE_ARRAY, decoded);
    }

    @Test
    public void testDecodeQuotedPrintableWithInvalidEncoding() {
        byte[] byteArray = new byte[7];
        byteArray[2] = ESCAPE_CHAR;
        try {
            QuotedPrintableCodec.decodeQuotedPrintable(byteArray);
            fail("Expected exception due to invalid encoding");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Invalid URL encoding"));
        }
    }

    @Test
    public void testDefaultCharsetIsUTF8() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertEquals(UTF_8, codec.getDefaultCharset());
    }

    @Test
    public void testEncodeDecodeStringWithDefaultCharset() throws Throwable {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String original = "Test String";
        String encoded = codec.encode(original);
        String decoded = codec.decode(encoded);
        assertEquals(original, decoded);
    }

    @Test
    public void testEncodeDecodeStringWithSpecifiedCharset() throws Throwable {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(DEFAULT_CHARSET);
        String original = "Test String";
        String encoded = codec.encode(original, DEFAULT_CHARSET);
        String decoded = codec.decode(encoded, DEFAULT_CHARSET);
        assertEquals(original, decoded);
    }

    @Test
    public void testEncodeDecodeWithNullInput() throws Throwable {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertNull(codec.encode((String) null));
        assertNull(codec.decode((String) null));
    }

    @Test
    public void testUnsupportedCharsetException() {
        try {
            new QuotedPrintableCodec("UnsupportedCharset");
            fail("Expected UnsupportedCharsetException");
        } catch (UnsupportedCharsetException e) {
            assertEquals("UnsupportedCharset", e.getMessage());
        }
    }

    @Test
    public void testIllegalCharsetNameException() {
        try {
            new QuotedPrintableCodec("Invalid Charset Name");
            fail("Expected IllegalCharsetNameException");
        } catch (IllegalCharsetNameException e) {
            assertEquals("Invalid Charset Name", e.getMessage());
        }
    }

    @Test
    public void testNullPointerExceptionForNullCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec((Charset) null);
        try {
            codec.encode("Test");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
}