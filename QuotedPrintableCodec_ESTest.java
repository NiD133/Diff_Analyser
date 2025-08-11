package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.BitSet;
import org.apache.commons.codec.net.QuotedPrintableCodec;
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
public class QuotedPrintableCodec_ESTest extends QuotedPrintableCodec_ESTest_scaffolding {

    // ========================================================================
    // Tests for static encodeQuotedPrintable method
    // ========================================================================

    @Test
    public void testEncodeQuotedPrintable_BitSetWithZeroBytes() {
        byte[] byteArray = new byte[8];
        long[] longArray = new long[9];
        longArray[0] = (long) (byte) 125;
        BitSet bitSet = BitSet.valueOf(longArray);
        byte[] result = QuotedPrintableCodec.encodeQuotedPrintable(bitSet, byteArray, true);
        assertEquals(8, result.length);
        assertArrayEquals(new byte[8], result);
    }

    @Test
    public void testEncodeQuotedPrintable_AllBytesRequireEncoding() {
        byte[] byteArray = new byte[3];
        BitSet bitSet = BitSet.valueOf(byteArray);
        byte[] result = QuotedPrintableCodec.encodeQuotedPrintable(bitSet, byteArray, true);
        byte[] expected = {(byte) '=', (byte) '0', (byte) '0', 
                          (byte) '=', (byte) '0', (byte) '0',
                          (byte) '=', (byte) '0', (byte) '0'};
        assertEquals(9, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testEncodeQuotedPrintable_NullBitSet() {
        byte[] byteArray = new byte[18];
        byte[] encoded = QuotedPrintableCodec.encodeQuotedPrintable(null, byteArray, false);
        byte[] doubleEncoded = QuotedPrintableCodec.encodeQuotedPrintable(null, encoded, true);
        byte[] decoded = QuotedPrintableCodec.decodeQuotedPrintable(doubleEncoded);
        assertEquals(54, decoded.length);
        assertEquals(93, doubleEncoded.length);
    }

    @Test
    public void testEncodeQuotedPrintable_WithSpaceAtEnd() {
        byte[] byteArray = new byte[5];
        byteArray[4] = (byte) 32; // Space character
        byte[] result = QuotedPrintableCodec.encodeQuotedPrintable(null, byteArray, true);
        assertNotNull(result);
        assertEquals(15, result.length);
    }

    @Test
    public void testEncodeQuotedPrintable_WithNonPrintableChars() {
        byte[] byteArray = new byte[9];
        byteArray[6] = (byte) -10; // Non-printable character
        byte[] result = QuotedPrintableCodec.encodeQuotedPrintable(null, byteArray, true);
        assertNotNull(result);
        assertEquals(27, result.length);
    }

    @Test
    public void testEncodeQuotedPrintable_WithCarriageReturn() {
        byte[] byteArray = new byte[9];
        byteArray[6] = (byte) 13; // CR character
        byte[] result = QuotedPrintableCodec.decodeQuotedPrintable(byteArray);
        assertArrayEquals(new byte[8], result);
    }

    @Test
    public void testEncodeQuotedPrintable_NullInput() {
        byte[] result = QuotedPrintableCodec.encodeQuotedPrintable(null, null, true);
        assertNull(result);
    }

    @Test
    public void testEncodeQuotedPrintable_EmptyInput() {
        byte[] byteArray = new byte[0];
        BitSet bitSet = new BitSet();
        byte[] result = QuotedPrintableCodec.encodeQuotedPrintable(bitSet, byteArray);
        assertArrayEquals(new byte[0], result);
    }

    // ========================================================================
    // Tests for static decodeQuotedPrintable method
    // ========================================================================

    @Test
    public void testDecodeQuotedPrintable_EmptyInput() {
        byte[] byteArray = new byte[0];
        byte[] result = QuotedPrintableCodec.decodeQuotedPrintable(byteArray);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    public void testDecodeQuotedPrintable_NullInput() {
        byte[] result = QuotedPrintableCodec.decodeQuotedPrintable(null);
        assertNull(result);
    }

    @Test
    public void testDecodeQuotedPrintable_WithSoftLineBreaks() {
        byte[] byteArray = new byte[27];
        byte[] encoded = QuotedPrintableCodec.encodeQuotedPrintable(null, byteArray, true);
        byte[] decoded = QuotedPrintableCodec.decodeQuotedPrintable(encoded);
        assertEquals(27, decoded.length);
        assertEquals(84, encoded.length);
    }

    // ========================================================================
    // Constructor tests
    // ========================================================================

    @Test
    public void testConstructor_DefaultCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertEquals("UTF-8", codec.getDefaultCharset());
    }

    @Test
    public void testConstructor_WithExplicitCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(Charset.defaultCharset());
        assertEquals(Charset.defaultCharset(), codec.getCharset());
    }

    @Test
    public void testConstructor_WithStrictMode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(true);
        assertTrue(codec.getCharset() != null); // Implementation detail check
    }

    @Test(expected = UnsupportedCharsetException.class)
    public void testConstructor_InvalidCharsetName() {
        new QuotedPrintableCodec("g");
    }

    @Test(expected = IllegalCharsetNameException.class)
    public void testConstructor_IllegalCharsetName() {
        new QuotedPrintableCodec("4>&$1^)O:\"");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullCharsetName() {
        new QuotedPrintableCodec((String) null);
    }

    // ========================================================================
    // Encoding functionality tests
    // ========================================================================

    @Test
    public void testEncode_StringWithSpecialChars() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(true);
        Charset charset = Charset.defaultCharset();
        String result = codec.encode("5-W&+N>@GrPC$hDz.$", charset);
        assertEquals("5-W&+N>@GrPC$hDz.$", result);
    }

    @Test
    public void testEncode_EmptyString() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String result = codec.encode("");
        assertEquals("", result);
    }

    @Test
    public void testEncode_NullInput() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertNull(codec.encode((String) null));
        assertNull(codec.encode((String) null, "UTF-8"));
        assertNull(codec.encode((byte[]) null));
    }

    @Test
    public void testEncode_ByteArray() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(false);
        byte[] input = new byte[11];
        byte[] result = codec.encode(input);
        assertNotSame(input, result);
    }

    @Test
    public void testEncode_ObjectHandling() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(true);
        Object result = codec.encode("Invalid URL encoding: not a valid digit (radix 16): ");
        assertEquals("Invalid URL encoding: not a valid digit (radix 16):=20", result);
    }

    // ========================================================================
    // Decoding functionality tests
    // ========================================================================

    @Test
    public void testDecode_QuotedString() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(true);
        Object result = codec.decode("Invalid URL encoding: not a valid digit (radix 16):=20");
        assertEquals("Invalid URL encoding: not a valid digit (radix 16): ", result);
    }

    @Test
    public void testDecode_EmptyString() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertEquals("", codec.decode(""));
        assertEquals("", codec.decode("", Charset.defaultCharset()));
    }

    @Test
    public void testDecode_NullInput() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertNull(codec.decode((String) null));
        assertNull(codec.decode((String) null, "UTF-8"));
        assertNull(codec.decode((byte[]) null));
    }

    @Test
    public void testDecode_ByteArray() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = new byte[2];
        byte[] result = codec.decode(input);
        assertArrayEquals(new byte[]{0, 0}, result);
    }

    // ========================================================================
    // Exception handling tests
    // ========================================================================

    @Test(expected = UnsupportedEncodingException.class)
    public void testEncode_UnsupportedEncoding() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.encode("?*Fid=v-(J", "?*Fid=v-(J");
    }

    @Test(expected = NullPointerException.class)
    public void testEncode_NullCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec((Charset) null);
        codec.encode("test");
    }

    @Test(expected = EncoderException.class)
    public void testEncode_InvalidObjectType() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.encode(codec);
    }

    @Test(expected = DecoderException.class)
    public void testDecode_InvalidQuotedPrintable() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.decode("V6Vz)'=Gpk}MM_");
    }

    @Test(expected = DecoderException.class)
    public void testDecode_InvalidObjectType() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.decode(codec);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testDecode_UnsupportedEncoding() throws Exception {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        codec.decode("text", "INVALID_ENCODING");
    }

    // ========================================================================
    // Edge case tests
    // ========================================================================

    @Test
    public void testGetCharset_NullCharsetHandling() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec((Charset) null);
        assertNull(codec.getCharset());
    }

    @Test
    public void testEncodeDecode_Consistency() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        byte[] input = new byte[0];
        byte[] encoded = codec.encode(input);
        byte[] decoded = codec.decode(encoded);
        assertArrayEquals(input, decoded);
    }

    @Test
    public void testEncode_WithExplicitCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec(Charset.defaultCharset(), true);
        String result = codec.encode(null, Charset.defaultCharset());
        assertNull(result);
    }

    @Test
    public void testDecode_WithExplicitCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String result = codec.decode(null, Charset.defaultCharset());
        assertNull(result);
    }
}