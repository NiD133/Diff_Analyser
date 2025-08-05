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

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class URLCodec_ESTest extends URLCodec_ESTest_scaffolding {

    // ======================================================
    // Tests for URLCodec static methods
    // ======================================================

    @Test(timeout = 4000)
    public void encodeUrl_withBitSet_encodesOnlyUnsafeBytes() {
        byte[] input = new byte[3];
        input[0] = (byte) 65; // 'A' (safe character)
        BitSet safeChars = BitSet.valueOf(input);
        
        byte[] result = URLCodec.encodeUrl(safeChars, input);
        assertArrayEquals("Safe bytes should be percent-encoded", 
            new byte[] {(byte)37, (byte)52, (byte)49, (byte)0, (byte)0}, 
            result
        );
    }

    @Test(timeout = 4000)
    public void encodeUrl_withEmptyArray_returnsNewEmptyArray() {
        byte[] emptyArray = new byte[0];
        BitSet safeChars = BitSet.valueOf(emptyArray);
        
        byte[] result = URLCodec.encodeUrl(safeChars, emptyArray);
        assertNotSame("Should return new empty array", emptyArray, result);
        assertEquals(0, result.length);
    }

    @Test(timeout = 4000)
    public void encodeUrl_withNullInput_returnsNull() {
        BitSet safeChars = URLCodec.WWW_FORM_URL;
        byte[] result = URLCodec.encodeUrl(safeChars, null);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void decodeUrl_withEmptyArray_returnsEmptyArray() {
        byte[] emptyArray = new byte[0];
        byte[] result = URLCodec.decodeUrl(emptyArray);
        assertArrayEquals(new byte[]{}, result);
    }

    @Test(timeout = 4000)
    public void decodeUrl_withNullInput_returnsNull() {
        byte[] result = URLCodec.decodeUrl(null);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void decodeUrl_withIncompleteEscapeSequence_throwsException() {
        byte[] invalidInput = new byte[]{(byte) '%'}; // Incomplete escape
        try {
            URLCodec.decodeUrl(invalidInput);
            fail("Expected DecoderException for incomplete escape sequence");
        } catch(Exception e) {
            verifyException("org.apache.commons.codec.net.URLCodec", e);
        }
    }

    @Test(timeout = 4000)
    public void decodeUrl_withPlusSign_decodesToSpace() {
        byte[] input = new byte[]{(byte) '+'};
        byte[] result = URLCodec.decodeUrl(input);
        assertArrayEquals("Plus should decode to space", 
            new byte[]{(byte) ' '}, 
            result
        );
    }

    // ======================================================
    // Tests for URLCodec instance methods
    // ======================================================

    @Test(timeout = 4000)
    public void encodeByteArray_withNull_returnsNull() {
        URLCodec codec = new URLCodec();
        assertNull(codec.encode((byte[]) null));
    }

    @Test(timeout = 4000)
    public void encodeByteArray_withEmptyArray_returnsEmptyArray() {
        URLCodec codec = new URLCodec();
        byte[] empty = new byte[0];
        byte[] result = codec.encode(empty);
        assertEquals(0, result.length);
    }

    @Test(timeout = 4000)
    public void encodeByteArray_withUnsafeBytes_encodesCorrectly() {
        URLCodec codec = new URLCodec();
        byte[] input = {0, 0, (byte) 0xC1, 0, 0, 0, 0}; // 0xC1 is unsafe
        
        byte[] encoded = codec.encode(input);
        byte[] decoded = codec.decode(encoded);
        
        assertArrayEquals("Should round-trip decode to original", input, decoded);
        assertEquals(21, encoded.length);
    }

    @Test(timeout = 4000)
    public void decodeByteArray_withNull_returnsNull() {
        URLCodec codec = new URLCodec();
        assertNull(codec.decode((byte[]) null));
    }

    @Test(timeout = 4000)
    public void decodeByteArray_withEmptyArray_returnsNewArray() {
        URLCodec codec = new URLCodec();
        byte[] empty = new byte[0];
        byte[] result = codec.decode(empty);
        assertNotSame(empty, result);
        assertEquals(0, result.length);
    }

    @Test(timeout = 4000)
    public void decodeByteArray_withIncompleteEscape_throwsException() {
        URLCodec codec = new URLCodec();
        byte[] invalid = { (byte) '%', 0 }; // Incomplete escape
        try {
            codec.decode(invalid);
            fail("Expected DecoderException for invalid escape sequence");
        } catch(Exception e) {
            verifyException("org.apache.commons.codec.net.Utils", e);
        }
    }

    @Test(timeout = 4000)
    public void encodeString_withNull_returnsNull() {
        URLCodec codec = new URLCodec();
        assertNull(codec.encode((String) null));
        assertNull(codec.encode(null, "UTF-8"));
    }

    @Test(timeout = 4000)
    public void encodeString_withEmptyString_returnsEmpty() {
        URLCodec codec = new URLCodec();
        assertEquals("", codec.encode(""));
        assertEquals("", codec.encode("", "UTF-8"));
    }

    @Test(timeout = 4000)
    public void encodeString_withSpecialCharacters_encodesCorrectly() {
        URLCodec codec = new URLCodec();
        assertEquals("VoM%3B", codec.encode("VoM;", "UTF-8"));
    }

    @Test(timeout = 4000)
    public void encodeString_withSpaces_encodesAsPlus() {
        URLCodec codec = new URLCodec();
        assertEquals("+can+be+encoded", 
            codec.encode(" can be encoded"));
    }

    @Test(timeout = 4000)
    public void encodeString_withUnsupportedCharset_throwsException() {
        URLCodec codec = new URLCodec();
        try {
            codec.encode("test", "INVALID_CHARSET");
            fail("Expected UnsupportedEncodingException");
        } catch(UnsupportedEncodingException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void encodeObject_withNonStringObject_throwsException() {
        URLCodec codec = new URLCodec();
        try {
            codec.encode(new Object());
            fail("Expected EncoderException for non-string object");
        } catch(Exception e) {
            verifyException("org.apache.commons.codec.net.URLCodec", e);
        }
    }

    @Test(timeout = 4000)
    public void decodeString_withNull_returnsNull() {
        URLCodec codec = new URLCodec();
        assertNull(codec.decode(null));
        assertNull(codec.decode(null, "UTF-8"));
    }

    @Test(timeout = 4000)
    public void decodeString_withEmptyString_returnsEmpty() {
        URLCodec codec = new URLCodec();
        assertEquals("", codec.decode(""));
        assertEquals("", codec.decode("", "UTF-8"));
    }

    @Test(timeout = 4000)
    public void decodeString_withPlusSign_decodesToSpace() {
        URLCodec codec = new URLCodec();
        assertEquals(" ", codec.decode("+"));
    }

    @Test(timeout = 4000)
    public void decodeString_withInvalidEscape_throwsException() {
        URLCodec codec = new URLCodec();
        try {
            codec.decode("%G0"); // Invalid hex digit
            fail("Expected DecoderException for invalid escape sequence");
        } catch(Exception e) {
            verifyException("org.apache.commons.codec.net.Utils", e);
        }
    }

    @Test(timeout = 4000)
    public void decodeObject_withNonStringObject_throwsException() {
        URLCodec codec = new URLCodec();
        try {
            codec.decode(new Object());
            fail("Expected DecoderException for non-string object");
        } catch(Exception e) {
            verifyException("org.apache.commons.codec.net.URLCodec", e);
        }
    }

    // ======================================================
    // Tests for charset handling methods
    // ======================================================

    @Test(timeout = 4000)
    public void getDefaultCharset_withDefaultConstructor_returnsUtf8() {
        URLCodec codec = new URLCodec();
        assertEquals("UTF-8", codec.getDefaultCharset());
    }

    @Test(timeout = 4000)
    public void getDefaultCharset_withCustomCharset_returnsCustom() {
        URLCodec codec = new URLCodec("ISO-8859-1");
        assertEquals("ISO-8859-1", codec.getDefaultCharset());
    }

    @Test(timeout = 4000)
    public void getEncoding_withDefaultConstructor_returnsUtf8() {
        URLCodec codec = new URLCodec();
        assertEquals("UTF-8", codec.getEncoding());
    }

    @Test(timeout = 4000)
    public void getEncoding_withNullCharset_returnsNull() {
        URLCodec codec = new URLCodec(null);
        codec.charset = null; // Simulate uninitialized charset
        assertNull(codec.getEncoding());
    }

    @Test(timeout = 4000)
    public void constructor_withNullCharset_handlesNullProperly() {
        URLCodec codec = new URLCodec(null);
        assertNull(codec.getDefaultCharset());
    }
}