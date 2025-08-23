package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.apache.commons.codec.net.URLCodec;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Test suite for the URLCodec class.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class URLCodec_ESTest extends URLCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testEncodeUrlWithBitSet() throws Throwable {
        byte[] inputBytes = new byte[] {65, 0, 0}; // ASCII for 'A'
        BitSet bitSet = BitSet.valueOf(inputBytes);
        byte[] encodedBytes = URLCodec.encodeUrl(bitSet, inputBytes);
        byte[] expectedBytes = new byte[] {37, 52, 49, 0, 0}; // Encoded '%41'
        assertArrayEquals(expectedBytes, encodedBytes);
    }

    @Test(timeout = 4000)
    public void testEncodeAndDecodeBytes() throws Throwable {
        URLCodec codec = new URLCodec();
        byte[] inputBytes = new byte[] {0, 0, -63, 0, 0, 0, 0};
        byte[] encodedBytes = codec.encode(inputBytes);
        byte[] decodedBytes = codec.decode(encodedBytes);
        assertArrayEquals(inputBytes, decodedBytes);
        assertEquals(7, decodedBytes.length);
        assertEquals(21, encodedBytes.length);
    }

    @Test(timeout = 4000)
    public void testDecodeInvalidUrlEncoding() throws Throwable {
        URLCodec codec = new URLCodec("`=G;6CyMxaWcZUTj%C");
        try {
            codec.decode("`=G;6CyMxaWcZUTj%C");
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.codec.net.URLCodec", e);
        }
    }

    @Test(timeout = 4000)
    public void testDecodeEmptyString() throws Throwable {
        URLCodec codec = new URLCodec("");
        try {
            codec.decode("");
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.codec.net.URLCodec", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetEncodingWhenCharsetIsNull() throws Throwable {
        URLCodec codec = new URLCodec();
        codec.charset = null;
        String encoding = codec.getEncoding();
        assertNull(encoding);
    }

    @Test(timeout = 4000)
    public void testGetDefaultEncoding() throws Throwable {
        URLCodec codec = new URLCodec();
        String encoding = codec.getEncoding();
        assertEquals("UTF-8", encoding);
    }

    @Test(timeout = 4000)
    public void testGetDefaultCharsetWhenNull() throws Throwable {
        URLCodec codec = new URLCodec((String) null);
        String charset = codec.getDefaultCharset();
        assertNull(charset);
    }

    @Test(timeout = 4000)
    public void testGetDefaultCharset() throws Throwable {
        URLCodec codec = new URLCodec("");
        String charset = codec.getDefaultCharset();
        assertEquals("", charset);
    }

    @Test(timeout = 4000)
    public void testEncodeEmptyByteArray() throws Throwable {
        byte[] inputBytes = new byte[0];
        BitSet bitSet = BitSet.valueOf(inputBytes);
        byte[] encodedBytes = URLCodec.encodeUrl(bitSet, inputBytes);
        assertNotSame(encodedBytes, inputBytes);
    }

    @Test(timeout = 4000)
    public void testEncodeEmptyByteArrayWithCodec() throws Throwable {
        URLCodec codec = new URLCodec();
        byte[] inputBytes = new byte[0];
        byte[] encodedBytes = codec.encode(inputBytes);
        assertEquals(0, encodedBytes.length);
    }

    @Test(timeout = 4000)
    public void testEncodeStringWithCharset() throws Throwable {
        URLCodec codec = new URLCodec();
        String encodedString = codec.encode("VoM;", "UTF-8");
        assertEquals("VoM%3B", encodedString);
    }

    @Test(timeout = 4000)
    public void testEncodeEmptyStringWithCharset() throws Throwable {
        URLCodec codec = new URLCodec();
        String encodedString = codec.encode("", "UTF-8");
        assertEquals("", encodedString);
    }

    @Test(timeout = 4000)
    public void testEncodeEmptyString() throws Throwable {
        URLCodec codec = new URLCodec();
        String encodedString = codec.encode("");
        assertNotNull(encodedString);
        assertEquals("", encodedString);
    }

    @Test(timeout = 4000)
    public void testDecodeEmptyByteArray() throws Throwable {
        byte[] inputBytes = new byte[0];
        byte[] decodedBytes = URLCodec.decodeUrl(inputBytes);
        assertArrayEquals(new byte[] {}, decodedBytes);
    }

    @Test(timeout = 4000)
    public void testDecodeNullByteArray() throws Throwable {
        URLCodec codec = new URLCodec("FHZLro0.RbE");
        byte[] decodedBytes = codec.decode((byte[]) null);
        assertNull(decodedBytes);
    }

    @Test(timeout = 4000)
    public void testDecodeEmptyByteArrayWithCodec() throws Throwable {
        URLCodec codec = new URLCodec();
        byte[] inputBytes = new byte[0];
        byte[] decodedBytes = codec.decode(inputBytes);
        assertNotSame(inputBytes, decodedBytes);
    }

    @Test(timeout = 4000)
    public void testDecodeStringWithCharset() throws Throwable {
        URLCodec codec = new URLCodec();
        String decodedString = codec.decode("UTF-8", "UTF-8");
        assertEquals("UTF-8", decodedString);
    }

    @Test(timeout = 4000)
    public void testDecodePlusSign() throws Throwable {
        URLCodec codec = new URLCodec();
        String decodedString = codec.decode("+");
        assertNotNull(decodedString);
        assertEquals(" ", decodedString);
    }

    @Test(timeout = 4000)
    public void testDecodeEmptyString() throws Throwable {
        URLCodec codec = new URLCodec();
        String decodedString = codec.decode("");
        assertNotNull(decodedString);
        assertEquals("", decodedString);
    }

    @Test(timeout = 4000)
    public void testDecodeObject() throws Throwable {
        URLCodec codec = new URLCodec();
        Object decodedObject = codec.decode((Object) "UTF-8");
        assertEquals("UTF-8", decodedObject);
        assertNotNull(decodedObject);
    }

    @Test(timeout = 4000)
    public void testEncodeWithNullCharset() throws Throwable {
        URLCodec codec = new URLCodec();
        try {
            codec.encode("org.apache.commons.codec.net.URLCodec", (String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testEncodeWithUnsupportedCharset() throws Throwable {
        URLCodec codec = new URLCodec("org.apache.commons.codec.DecoderException");
        try {
            codec.encode("org.apache.commons.codec.DecoderException");
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.codec.net.URLCodec", e);
        }
    }

    @Test(timeout = 4000)
    public void testEncodeWithNullCharsetAndEmptyString() throws Throwable {
        URLCodec codec = new URLCodec((String) null);
        try {
            codec.encode("");
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testEncodeWithNullCharsetAndObject() throws Throwable {
        URLCodec codec = new URLCodec((String) null);
        try {
            codec.encode((Object) "4r9MeMtOx{]6dKda");
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testDecodeInvalidByteArray() throws Throwable {
        URLCodec codec = new URLCodec();
        byte[] inputBytes = new byte[] {37, 0};
        try {
            codec.decode(inputBytes);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.codec.net.Utils", e);
        }
    }

    @Test(timeout = 4000)
    public void testDecodeWithInvalidEncoding() throws Throwable {
        URLCodec codec = new URLCodec();
        try {
            codec.decode("#6|\"J%kyWIe[[:g", "#6|\"J%kyWIe[[:g");
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.codec.net.Utils", e);
        }
    }

    @Test(timeout = 4000)
    public void testDecodeWithNullCharset() throws Throwable {
        URLCodec codec = new URLCodec();
        try {
            codec.decode("_iXXSIH;6c", (String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testDecodeWithUnsupportedEncoding() throws Throwable {
        URLCodec codec = new URLCodec();
        try {
            codec.decode("org.apache.commons.codec.DecoderException", "org.apache.commons.codec.DecoderException");
            fail("Expecting exception: UnsupportedEncodingException");
        } catch (UnsupportedEncodingException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testDecodeWithNullCharsetAndString() throws Throwable {
        URLCodec codec = new URLCodec((String) null);
        try {
            codec.decode("8");
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testEncodeWithUnsupportedEncoding() throws Throwable {
        URLCodec codec = new URLCodec("org.apache.commons.codec.CharEncoding");
        try {
            codec.encode("org.apache.commons.codec.CharEncoding", "org.apache.commons.codec.CharEncoding");
            fail("Expecting exception: UnsupportedEncodingException");
        } catch (UnsupportedEncodingException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testEncodeUrlWithBitSetAndByteArray() throws Throwable {
        byte[] inputBytes = new byte[] {0, 0, 0, -93, 0, 0, 0, 0};
        BitSet bitSet = URLCodec.WWW_FORM_URL;
        byte[] encodedBytes = URLCodec.encodeUrl(bitSet, inputBytes);
        assertEquals(24, encodedBytes.length);
    }

    @Test(timeout = 4000)
    public void testEncodeUrlWithNullByteArray() throws Throwable {
        BitSet bitSet = URLCodec.WWW_FORM_URL;
        byte[] encodedBytes = URLCodec.encodeUrl(bitSet, (byte[]) null);
        assertNull(encodedBytes);
    }

    @Test(timeout = 4000)
    public void testDecodeInvalidUrlEncodingWithSinglePercent() throws Throwable {
        byte[] inputBytes = new byte[] {37};
        try {
            URLCodec.decodeUrl(inputBytes);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.codec.net.URLCodec", e);
        }
    }

    @Test(timeout = 4000)
    public void testEncodeNullStringWithNullCharset() throws Throwable {
        URLCodec codec = new URLCodec();
        String encodedString = codec.encode((String) null, (String) null);
        assertNull(encodedString);
    }

    @Test(timeout = 4000)
    public void testEncodeNullString() throws Throwable {
        URLCodec codec = new URLCodec();
        String encodedString = codec.encode((String) null);
        assertNull(encodedString);
    }

    @Test(timeout = 4000)
    public void testEncodeObjectWithException() throws Throwable {
        URLCodec codec = new URLCodec();
        try {
            codec.encode((Object) codec);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.codec.net.URLCodec", e);
        }
    }

    @Test(timeout = 4000)
    public void testEncodeNullObject() throws Throwable {
        URLCodec codec = new URLCodec();
        Object encodedObject = codec.encode((Object) null);
        assertNull(encodedObject);
    }

    @Test(timeout = 4000)
    public void testEncodeObject() throws Throwable {
        URLCodec codec = new URLCodec();
        Object encodedObject = codec.encode((Object) " cannot be URL decoded");
        assertNotNull(encodedObject);
        assertEquals("+cannot+be+URL+decoded", encodedObject);
    }

    @Test(timeout = 4000)
    public void testDecodeNullStringWithCharset() throws Throwable {
        URLCodec codec = new URLCodec("");
        String decodedString = codec.decode((String) null, "B(*(.00*s");
        assertNull(decodedString);
    }

    @Test(timeout = 4000)
    public void testDecodeNullString() throws Throwable {
        URLCodec codec = new URLCodec();
        String decodedString = codec.decode((String) null);
        assertNull(decodedString);
    }

    @Test(timeout = 4000)
    public void testDecodeNullObjectWithException() throws Throwable {
        URLCodec codec = new URLCodec((String) null);
        try {
            codec.decode((Object) "");
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testDecodeNullObject() throws Throwable {
        URLCodec codec = new URLCodec();
        Object decodedObject = codec.decode((Object) null);
        assertNull(decodedObject);
    }

    @Test(timeout = 4000)
    public void testDecodeObjectWithException() throws Throwable {
        URLCodec codec = new URLCodec("3X{`l{LuA3.3r K44");
        Object object = new Object();
        try {
            codec.decode(object);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.codec.net.URLCodec", e);
        }
    }

    @Test(timeout = 4000)
    public void testEncodeString() throws Throwable {
        URLCodec codec = new URLCodec();
        String encodedString = codec.encode("org.apache.commons.codec.DecoderException");
        assertEquals("org.apache.commons.codec.DecoderException", encodedString);
        assertNotNull(encodedString);
    }

    @Test(timeout = 4000)
    public void testEncodeUrlWithNullBitSet() throws Throwable {
        byte[] inputBytes = new byte[9];
        byte[] encodedBytes = URLCodec.encodeUrl((BitSet) null, inputBytes);
        assertEquals(27, encodedBytes.length);
    }

    @Test(timeout = 4000)
    public void testEncodeNullByteArray() throws Throwable {
        URLCodec codec = new URLCodec();
        byte[] encodedBytes = codec.encode((byte[]) null);
        assertNull(encodedBytes);
    }

    @Test(timeout = 4000)
    public void testDecodeUrlAndEncodeUrl() throws Throwable {
        byte[] inputBytes = new byte[] {43, 0, 0, 0, 0, 0, 0, 0}; // '+' character
        byte[] decodedBytes = URLCodec.decodeUrl(inputBytes);
        BitSet bitSet = URLCodec.WWW_FORM_URL;
        byte[] encodedBytes = URLCodec.encodeUrl(bitSet, decodedBytes);
        assertArrayEquals(new byte[] {32, 0, 0, 0, 0, 0, 0, 0}, decodedBytes); // ' ' character
        assertEquals(22, encodedBytes.length);
        assertNotNull(encodedBytes);
    }

    @Test(timeout = 4000)
    public void testDecodeNullByteArray() throws Throwable {
        byte[] decodedBytes = URLCodec.decodeUrl((byte[]) null);
        assertNull(decodedBytes);
    }

    @Test(timeout = 4000)
    public void testDecodeStringWithCharset() throws Throwable {
        URLCodec codec = new URLCodec();
        String decodedString = codec.decode("", "UTF-8");
        assertEquals("", decodedString);
        assertNotNull(decodedString);
    }

    @Test(timeout = 4000)
    public void testGetEncodingForEmptyCharset() throws Throwable {
        URLCodec codec = new URLCodec("");
        String encoding = codec.getEncoding();
        assertEquals("", encoding);
    }

    @Test(timeout = 4000)
    public void testGetDefaultCharset() throws Throwable {
        URLCodec codec = new URLCodec();
        String charset = codec.getDefaultCharset();
        assertEquals("UTF-8", charset);
    }
}