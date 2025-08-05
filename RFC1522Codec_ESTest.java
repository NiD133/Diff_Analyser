package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import org.apache.commons.codec.CodecPolicy;
import org.apache.commons.codec.net.BCodec;
import org.apache.commons.codec.net.QCodec;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class RFC1522Codec_ESTest extends RFC1522Codec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testEncodeNullTextWithQCodec() throws Throwable {
        QCodec qCodec = new QCodec();
        Charset defaultCharset = Charset.defaultCharset();
        String encodedText = qCodec.encodeText(null, defaultCharset);
        assertNull("Encoding null text should return null", encodedText);
    }

    @Test(timeout = 4000)
    public void testEncodeMalformedTextWithBCodec() throws Throwable {
        QCodec qCodec = new QCodec();
        Charset charset = qCodec.getCharset();
        CodecPolicy codecPolicy = CodecPolicy.STRICT;
        BCodec bCodec = new BCodec(charset, codecPolicy);
        String encodedText = bCodec.encodeText("=?=?=?UTF-8?Q?Q?=", charset);
        assertNotNull("Encoded text should not be null", encodedText);
        assertEquals("=?UTF-8?B?PT89Pz0/VVRGLTg/UT9RPz0=?=", encodedText);
    }

    @Test(timeout = 4000)
    public void testEncodeNullTextWithBCodec() throws Throwable {
        BCodec bCodec = new BCodec();
        String encodedText = bCodec.encodeText(null, "RFC 1522 violation: malformed encoded content");
        assertNull("Encoding null text should return null", encodedText);
    }

    @Test(timeout = 4000)
    public void testDecodeNullTextWithQCodec() throws Throwable {
        QCodec qCodec = new QCodec();
        String decodedText = qCodec.decodeText(null);
        assertNull("Decoding null text should return null", decodedText);
    }

    @Test(timeout = 4000)
    public void testDecodeValidTextWithBCodec() throws Throwable {
        QCodec qCodec = new QCodec();
        Charset charset = qCodec.getCharset();
        CodecPolicy codecPolicy = CodecPolicy.STRICT;
        BCodec bCodec = new BCodec(charset, codecPolicy);
        String decodedText = bCodec.decodeText("=?UTF-8?B?PT9eLT89Pz0=?=");
        assertEquals("=?^-?=?=", decodedText);
    }

    @Test(timeout = 4000)
    public void testDecodeEmptyEncodedTextWithBCodec() throws Throwable {
        BCodec bCodec = new BCodec();
        String decodedText = bCodec.decodeText("=?UTF-8?B??=");
        assertEquals("Decoding empty encoded text should return an empty string", "", decodedText);
    }

    @Test(timeout = 4000)
    public void testEncodeTextWithNullCharsetThrowsException() throws Throwable {
        QCodec qCodec = new QCodec();
        try {
            qCodec.encodeText("{i", null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testEncodeTextWithUnsupportedCharsetThrowsException() throws Throwable {
        BCodec bCodec = new BCodec();
        try {
            bCodec.encodeText("org.apache.commons.codec.net.RFC1522Codec", "org.apache.commons.codec.net.RFC1522Codec");
            fail("Expecting exception: UnsupportedCharsetException");
        } catch (UnsupportedCharsetException e) {
            verifyException("java.nio.charset.Charset", e);
        }
    }

    @Test(timeout = 4000)
    public void testEncodeTextWithIllegalCharsetNameThrowsException() throws Throwable {
        BCodec bCodec = new BCodec();
        try {
            bCodec.encodeText("This codec cannot decode ", "This codec cannot decode ");
            fail("Expecting exception: IllegalCharsetNameException");
        } catch (IllegalCharsetNameException e) {
            verifyException("java.nio.charset.Charset", e);
        }
    }

    @Test(timeout = 4000)
    public void testEncodeTextWithNullCharsetNameThrowsException() throws Throwable {
        BCodec bCodec = new BCodec();
        try {
            bCodec.encodeText("0T5`BTkU*|f-hr", null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("java.nio.charset.Charset", e);
        }
    }

    @Test(timeout = 4000)
    public void testDecodeMalformedTextThrowsStringIndexOutOfBoundsException() throws Throwable {
        BCodec bCodec = new BCodec();
        try {
            bCodec.decodeText("=?=");
            fail("Expecting exception: StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testDecodeTextWithUnsupportedEncodingThrowsException() throws Throwable {
        BCodec bCodec = new BCodec();
        try {
            bCodec.decodeText("=?TF-?B??=?=");
            fail("Expecting exception: UnsupportedEncodingException");
        } catch (UnsupportedEncodingException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testEncodeTextWithBCodec() throws Throwable {
        BCodec bCodec = new BCodec();
        String encodedText = bCodec.encodeText("@~_=I", "UTF-8");
        assertNotNull("Encoded text should not be null", encodedText);
        assertEquals("=?UTF-8?B?QH5fPUk=?=", encodedText);
    }

    @Test(timeout = 4000)
    public void testDecodeQEncodedContentThrowsException() throws Throwable {
        BCodec bCodec = new BCodec();
        try {
            bCodec.decodeText("=?UTF-8?Q?Q?=?=");
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.codec.net.RFC1522Codec", e);
        }
    }

    @Test(timeout = 4000)
    public void testDecodeTextWithMissingEncodingTokenThrowsException() throws Throwable {
        BCodec bCodec = new BCodec();
        try {
            bCodec.decodeText("=?=?SRq9'.C?=");
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.codec.net.RFC1522Codec", e);
        }
    }

    @Test(timeout = 4000)
    public void testDecodeTextWithMissingCharsetThrowsException() throws Throwable {
        BCodec bCodec = new BCodec();
        try {
            bCodec.decodeText("=??-?=");
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.codec.net.RFC1522Codec", e);
        }
    }

    @Test(timeout = 4000)
    public void testDecodeTextWithMissingCharsetTokenThrowsException() throws Throwable {
        BCodec bCodec = new BCodec();
        try {
            bCodec.decodeText("=?^-?=");
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.codec.net.RFC1522Codec", e);
        }
    }

    @Test(timeout = 4000)
    public void testDecodeMalformedEncodedContentThrowsException() throws Throwable {
        BCodec bCodec = new BCodec();
        try {
            bCodec.decodeText("=?=?=?ZCg05nk5fYK>>");
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.codec.net.RFC1522Codec", e);
        }
    }

    @Test(timeout = 4000)
    public void testDecodeEmptyTextThrowsException() throws Throwable {
        BCodec bCodec = new BCodec();
        try {
            bCodec.decodeText("");
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.codec.net.RFC1522Codec", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetDefaultCharset() throws Throwable {
        BCodec bCodec = new BCodec();
        String defaultCharset = bCodec.getDefaultCharset();
        assertEquals("Default charset should be UTF-8", "UTF-8", defaultCharset);
    }
}