package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import org.apache.commons.codec.CodecPolicy;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class BCodecTest {

    @Test(timeout = 4000)
    public void testDefaultEncodingIsB() throws Throwable {
        BCodec bCodec = new BCodec();
        String encoding = bCodec.getEncoding();
        assertEquals("B", encoding);
    }

    @Test(timeout = 4000)
    public void testEncodeNullStringReturnsNull() throws Throwable {
        Charset charset = Charset.defaultCharset();
        CodecPolicy codecPolicy = CodecPolicy.STRICT;
        BCodec bCodec = new BCodec(charset, codecPolicy);
        String result = bCodec.encode((String) null, charset);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testEncodeStringWithDefaultCharset() throws Throwable {
        BCodec bCodec = new BCodec();
        Charset charset = Charset.defaultCharset();
        String result = bCodec.encode("^QOTD7,4PZ$(<r", charset);
        assertEquals("=?UTF-8?B?XlFPVEQ3LDRQWiQoPHI=?=", result);
    }

    @Test(timeout = 4000)
    public void testEncodeNullStringWithNullCharsetReturnsNull() throws Throwable {
        BCodec bCodec = new BCodec();
        String result = bCodec.encode((String) null, (String) null);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testEncodeNullStringReturnsNull() throws Throwable {
        BCodec bCodec = new BCodec();
        String result = bCodec.encode((String) null);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testDoEncodingWithByteArray() throws Throwable {
        Charset charset = Charset.defaultCharset();
        BCodec bCodec = new BCodec(charset);
        byte[] byteArray = new byte[7];
        byte[] encodedArray = bCodec.doEncoding(byteArray);
        assertEquals(12, encodedArray.length);
    }

    @Test(timeout = 4000)
    public void testDoDecodingAndEncodingWithByteArray() throws Throwable {
        BCodec bCodec = new BCodec();
        byte[] byteArray = new byte[6];
        byte[] decodedArray = bCodec.doDecoding(byteArray);
        byte[] encodedArray = bCodec.doEncoding(decodedArray);
        assertNotSame(encodedArray, byteArray);
    }

    @Test(timeout = 4000)
    public void testDoDecodingWithModifiedByteArray() throws Throwable {
        BCodec bCodec = new BCodec();
        byte[] byteArray = new byte[7];
        byteArray[0] = (byte) 52;
        byteArray[6] = (byte) 75;
        byte[] decodedArray = bCodec.doDecoding(byteArray);
        assertEquals(1, decodedArray.length);
    }

    @Test(timeout = 4000)
    public void testDecodeNullStringReturnsNull() throws Throwable {
        BCodec bCodec = new BCodec();
        String result = bCodec.decode((String) null);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testDecodeEmptyEncodedString() throws Throwable {
        Charset charset = Charset.defaultCharset();
        BCodec bCodec = new BCodec(charset);
        String result = bCodec.decode("=?UTF-8?B??=");
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testEncodeStringWithInvalidCharsetThrowsException() throws Throwable {
        BCodec bCodec = new BCodec();
        try {
            bCodec.encode("B", "B");
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.codec.net.BCodec", e);
        }
    }

    @Test(timeout = 4000)
    public void testEncodeWithIllegalCharsetNameThrowsException() throws Throwable {
        BCodec bCodec = new BCodec();
        try {
            bCodec.encode("gK1C.|((", "");
            fail("Expecting exception: IllegalCharsetNameException");
        } catch (IllegalCharsetNameException e) {
            verifyException("java.nio.charset.Charset", e);
        }
    }

    @Test(timeout = 4000)
    public void testEncodeWithNullCharsetThrowsException() throws Throwable {
        BCodec bCodec = new BCodec();
        try {
            bCodec.encode(" encoded content", (String) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("java.nio.charset.Charset", e);
        }
    }

    @Test(timeout = 4000)
    public void testDecodeWithNullCodecPolicyThrowsException() throws Throwable {
        Charset charset = Charset.defaultCharset();
        BCodec bCodec = new BCodec(charset, (CodecPolicy) null);
        byte[] byteArray = new byte[22];
        try {
            bCodec.doDecoding(byteArray);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testStrictDecodingWithInvalidDataThrowsException() throws Throwable {
        Charset charset = Charset.defaultCharset();
        CodecPolicy codecPolicy = CodecPolicy.STRICT;
        BCodec bCodec = new BCodec(charset, codecPolicy);
        byte[] byteArray = new byte[22];
        byteArray[3] = (byte) 45;
        try {
            bCodec.doDecoding(byteArray);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.codec.binary.Base64", e);
        }
    }

    @Test(timeout = 4000)
    public void testDecodeMalformedEncodedContentThrowsException() throws Throwable {
        BCodec bCodec = new BCodec();
        try {
            bCodec.decode("WgJp7)");
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.codec.net.RFC1522Codec", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithNullCharsetThrowsException() throws Throwable {
        CodecPolicy codecPolicy = CodecPolicy.STRICT;
        try {
            new BCodec((Charset) null, codecPolicy);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithNullCharsetNameThrowsException() throws Throwable {
        try {
            new BCodec((Charset) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithUnsupportedCharsetThrowsException() throws Throwable {
        try {
            new BCodec("p-Ubb");
            fail("Expecting exception: UnsupportedCharsetException");
        } catch (UnsupportedCharsetException e) {
            verifyException("java.nio.charset.Charset", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithIllegalCharsetNameThrowsException() throws Throwable {
        try {
            new BCodec("encodeTable must have exactly 64 entries.");
            fail("Expecting exception: IllegalCharsetNameException");
        } catch (IllegalCharsetNameException e) {
            verifyException("java.nio.charset.Charset", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithNullCharsetNameThrowsException() throws Throwable {
        try {
            new BCodec((String) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("java.nio.charset.Charset", e);
        }
    }

    @Test(timeout = 4000)
    public void testEncodeWithNullCharsetThrowsException() throws Throwable {
        BCodec bCodec = new BCodec();
        try {
            bCodec.encode(".LC", (Charset) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testIsStrictDecodingReturnsTrueForStrictPolicy() throws Throwable {
        Charset charset = Charset.defaultCharset();
        CodecPolicy codecPolicy = CodecPolicy.STRICT;
        BCodec bCodec = new BCodec(charset, codecPolicy);
        assertTrue(bCodec.isStrictDecoding());
    }

    @Test(timeout = 4000)
    public void testIsStrictDecodingReturnsFalseForDefaultPolicy() throws Throwable {
        BCodec bCodec = new BCodec();
        assertFalse(bCodec.isStrictDecoding());
    }

    @Test(timeout = 4000)
    public void testEncodeObjectReturnsEncodedString() throws Throwable {
        BCodec bCodec = new BCodec();
        Object result = bCodec.encode((Object) " encoded content");
        assertEquals("=?UTF-8?B?IGVuY29kZWQgY29udGVudA==?=", result);
    }

    @Test(timeout = 4000)
    public void testEncodeNullObjectReturnsNull() throws Throwable {
        BCodec bCodec = new BCodec();
        Object result = bCodec.encode((Object) null);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testEncodeNonStringObjectThrowsException() throws Throwable {
        BCodec bCodec = new BCodec();
        try {
            bCodec.encode((Object) bCodec);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.codec.net.BCodec", e);
        }
    }

    @Test(timeout = 4000)
    public void testDoEncodingWithNullByteArrayReturnsNull() throws Throwable {
        BCodec bCodec = new BCodec();
        byte[] result = bCodec.doEncoding((byte[]) null);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testDoDecodingWithNullByteArrayReturnsNull() throws Throwable {
        BCodec bCodec = new BCodec();
        byte[] result = bCodec.doDecoding((byte[]) null);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testDecodeNonStringObjectThrowsException() throws Throwable {
        BCodec bCodec = new BCodec();
        try {
            bCodec.decode((Object) bCodec);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.codec.net.BCodec", e);
        }
    }

    @Test(timeout = 4000)
    public void testDecodeNullObjectReturnsNull() throws Throwable {
        BCodec bCodec = new BCodec();
        Object result = bCodec.decode((Object) null);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testDecodeEncodedStringReturnsOriginalString() throws Throwable {
        BCodec bCodec = new BCodec();
        Object result = bCodec.decode((Object) "=?UTF-8?B?LHcuLHAlKw==?=");
        assertEquals(",w.,p%+", result);
    }

    @Test(timeout = 4000)
    public void testDecodeEncodedStringReturnsDecodedString() throws Throwable {
        BCodec bCodec = new BCodec();
        String result = bCodec.decode("=?UTF-8?B?IGVuY29kZWQgY29udGVudA==?=");
        assertEquals(" encoded content", result);
    }

    @Test(timeout = 4000)
    public void testEncodeStringWithSpecificCharset() throws Throwable {
        BCodec bCodec = new BCodec("l9");
        String result = bCodec.encode("l9");
        assertEquals("=?ISO-8859-15?B?bDk=?=", result);
    }

    @Test(timeout = 4000)
    public void testEncodeStringWithSpecificCharsetName() throws Throwable {
        BCodec bCodec = new BCodec("l9");
        String result = bCodec.encode("l9", "l9");
        assertEquals("=?ISO-8859-15?B?bDk=?=", result);
    }
}