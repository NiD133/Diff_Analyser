package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.codec.CodecPolicy;
import org.apache.commons.codec.binary.Base16;
import org.apache.commons.codec.binary.BaseNCodec;

/**
 * Comprehensive test suite for Base16 encoding and decoding functionality.
 * Tests cover normal operations, edge cases, error conditions, and policy enforcement.
 */
public class Base16Test {

    // Test Constants
    private static final byte INVALID_CHAR_G = (byte) 71; // 'G' - not in Base16 alphabet
    private static final byte INVALID_CHAR_BACKSLASH = (byte) 92; // '\' - not in Base16 alphabet
    private static final byte VALID_CHAR_ZERO = (byte) 48; // '0' - valid Base16 character
    private static final byte NEGATIVE_BYTE_VALUE = (byte) -23; // 0xE9 in hex
    private static final String ENCODED_E9 = "E9"; // hex representation of -23
    private static final String INVALID_HEX_WITH_G = "Gw"; // contains invalid 'G'
    private static final String INVALID_HEX_WITH_T = "BDT"; // contains invalid 'T'
    private static final String VALID_HEX_21 = "21"; // valid hex string
    private static final String SINGLE_HEX_CHAR = "F"; // incomplete hex pair
    private static final byte EXPECTED_BYTE_21 = (byte) 33; // decimal 33 = hex 21

    // ========== Alphabet Validation Tests ==========

    @Test(timeout = 4000)
    public void testIsInAlphabet_InvalidCharacter_G() {
        Base16 encoder = new Base16();
        boolean result = encoder.isInAlphabet(INVALID_CHAR_G);
        assertFalse("Character 'G' should not be in Base16 alphabet", result);
    }

    @Test(timeout = 4000)
    public void testIsInAlphabet_InvalidCharacter_Backslash() {
        Base16 encoder = new Base16();
        boolean result = encoder.isInAlphabet(INVALID_CHAR_BACKSLASH);
        assertFalse("Backslash character should not be in Base16 alphabet", result);
    }

    @Test(timeout = 4000)
    public void testIsInAlphabet_ValidCharacter_Zero() {
        Base16 encoder = new Base16();
        boolean result = encoder.isInAlphabet(VALID_CHAR_ZERO);
        assertTrue("Character '0' should be in Base16 alphabet", result);
    }

    @Test(timeout = 4000)
    public void testContainsAlphabetOrPad_ChunkSeparator() {
        Base16 encoder = new Base16();
        byte[] chunkSeparator = BaseNCodec.CHUNK_SEPARATOR;
        boolean result = encoder.containsAlphabetOrPad(chunkSeparator);
        assertFalse("Chunk separator should not contain alphabet or pad characters", result);
    }

    // ========== Encoding Tests ==========

    @Test(timeout = 4000)
    public void testEncode_ByteArrayWithNegativeValue() {
        Base16 encoder = createStrictBase16Encoder();
        byte[] inputData = createByteArrayWithNegativeValue();

        byte[] encodedResult = encoder.encode(inputData);

        byte[] expectedEncoding = {(byte)'0', (byte)'0', (byte)'E', (byte)'9', (byte)'0', (byte)'0'};
        assertArrayEquals("Encoding should convert negative byte to correct hex representation",
                         expectedEncoding, encodedResult);
    }

    @Test(timeout = 4000)
    public void testEncode_WithContext_ZeroLength() {
        Base16 encoder = new Base16();
        byte[] inputData = new byte[4];
        BaseNCodec.Context context = new BaseNCodec.Context();

        encoder.encode(inputData, 0, 0, context);

        assertArrayEquals("Zero-length encoding should not modify input array",
                         new byte[4], inputData);
    }

    @Test(timeout = 4000)
    public void testEncode_EmptyByteArray() {
        Base16 encoder = new Base16();
        byte[] inputData = new byte[5];

        byte[] result = encoder.encode(inputData, -1832, -1832);

        assertNotSame("Encoded result should be a different array instance", inputData, result);
    }

    @Test(timeout = 4000)
    public void testEncode_ValidRangeWithLargeOffset() {
        Base16 encoder = new Base16();
        byte[] inputData = new byte[5];
        BaseNCodec.Context context = new BaseNCodec.Context();

        // This should not throw an exception for valid range
        encoder.encode(inputData, Integer.MAX_VALUE - 158, 484, context);

        assertEquals("MIME chunk size should be 76", 76, BaseNCodec.MIME_CHUNK_SIZE);
    }

    // ========== Encoding Error Cases ==========

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testEncode_ExceedsMaximumSize() {
        Base16 encoder = new Base16();
        byte[] inputData = new byte[6];
        BaseNCodec.Context context = new BaseNCodec.Context();

        encoder.encode(inputData, 2131, Integer.MAX_VALUE - 8, context);
        fail("Should throw IllegalArgumentException for input exceeding maximum size");
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testEncode_NullContext() {
        Base16 encoder = new Base16();
        byte[] inputData = new byte[3];

        encoder.encode(inputData, 76, 0, null);
        fail("Should throw NullPointerException for null context");
    }

    @Test(timeout = 4000, expected = ArrayIndexOutOfBoundsException.class)
    public void testEncode_InvalidArrayAccess() {
        Base16 encoder = new Base16();
        byte[] inputData = new byte[6];
        BaseNCodec.Context context = new BaseNCodec.Context();

        encoder.encode(inputData, 76, 76, context);
        fail("Should throw ArrayIndexOutOfBoundsException for invalid array access");
    }

    // ========== Decoding Tests ==========

    @Test(timeout = 4000)
    public void testDecode_ValidHexString() {
        Base16 decoder = new Base16();

        byte[] result = decoder.decode(VALID_HEX_21);

        assertArrayEquals("Should correctly decode hex '21' to byte 33",
                         new byte[]{EXPECTED_BYTE_21}, result);
    }

    @Test(timeout = 4000)
    public void testDecode_WithNegativeOffsets() {
        Base16 decoder = new Base16();
        byte[] inputData = new byte[3];
        BaseNCodec.Context context = new BaseNCodec.Context();
        context.ibitWorkArea = -4039;

        decoder.decode(inputData, -3424, -3424, context);

        assertArrayEquals("Decoding with negative offsets should not modify array",
                         new byte[3], inputData);
    }

    @Test(timeout = 4000)
    public void testDecode_RepeatedCalls() {
        Base16 decoder = new Base16();
        byte[] inputData = new byte[10];
        BaseNCodec.Context context = new BaseNCodec.Context();

        decoder.decode(inputData, -1800, -1800, context);
        decoder.decode(inputData, -1800, -1800, context);

        assertArrayEquals("Repeated decode calls should not modify zero array",
                         new byte[10], inputData);
    }

    // ========== Decoding Error Cases ==========

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testDecode_InvalidCharacter_G() {
        Base16 decoder = new Base16();

        decoder.decode(INVALID_HEX_WITH_G);
        fail("Should throw IllegalArgumentException for invalid character 'G'");
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testDecode_InvalidCharacter_T() {
        Base16 decoder = new Base16();

        decoder.decode(INVALID_HEX_WITH_T);
        fail("Should throw IllegalArgumentException for invalid character 'T'");
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testDecode_InvalidOctetInByteArray() {
        Base16 decoder = new Base16();
        byte[] inputData = new byte[7];
        BaseNCodec.Context context = new BaseNCodec.Context();

        decoder.decode(inputData, 6, 6, context);
        fail("Should throw IllegalArgumentException for invalid octet (0)");
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testDecode_StrictPolicy_OddLengthString() {
        Base16 decoder = createStrictBase16Decoder();

        decoder.decode(SINGLE_HEX_CHAR);
        fail("Strict decoding should reject single character (incomplete pair)");
    }

    @Test(timeout = 4000, expected = ArrayIndexOutOfBoundsException.class)
    public void testDecode_InvalidArrayOffset() {
        Base16 decoder = createStrictBase16Decoder();
        byte[] inputData = new byte[3];
        BaseNCodec.Context context = new BaseNCodec.Context();
        context.ibitWorkArea = NEGATIVE_BYTE_VALUE;

        decoder.decode(inputData, 4, 64, context);
        fail("Should throw ArrayIndexOutOfBoundsException for invalid offset");
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testDecode_NullByteArray() {
        Base16 decoder = new Base16();
        BaseNCodec.Context context = new BaseNCodec.Context();

        decoder.decode(null, 64, 64, context);
        fail("Should throw NullPointerException for null byte array");
    }

    @Test(timeout = 4000, expected = ArrayIndexOutOfBoundsException.class)
    public void testDecode_LenientPolicy_InvalidOffset() {
        Base16 decoder = createLenientBase16Decoder();
        byte[] inputData = new byte[1];
        BaseNCodec.Context context = new BaseNCodec.Context();
        context.ibitWorkArea = 76;

        decoder.decode(inputData, 1, 1, context);
        fail("Should throw ArrayIndexOutOfBoundsException even with lenient policy");
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testDecode_LenientPolicy_InvalidOctet() {
        Base16 decoder = createLenientBase16Decoder();
        byte[] inputData = new byte[19];
        BaseNCodec.Context context = new BaseNCodec.Context();
        context.ibitWorkArea = 76;

        decoder.decode(inputData, 0, 0, context);
        fail("Should still throw IllegalArgumentException for invalid octets in lenient mode");
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testDecode_WithNonZeroByteAfterE() {
        Base16 decoder = new Base16();
        BaseNCodec.Context context = new BaseNCodec.Context();
        context.ibitWorkArea = 64;
        byte[] inputData = new byte[7];
        inputData[5] = (byte) 69; // 'E' character

        decoder.decode(inputData, 5, 513, context);
        fail("Should throw IllegalArgumentException for invalid octet sequence");
    }

    // ========== Constructor Tests ==========

    @Test(timeout = 4000)
    public void testConstructor_LowerCase() {
        Base16 encoder = new Base16(false);
        assertEquals("PEM chunk size should be 64", 64, BaseNCodec.PEM_CHUNK_SIZE);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testConstructor_NullCodecPolicy() {
        new Base16(true, null);
        fail("Should throw NullPointerException for null codec policy");
    }

    // ========== Helper Methods ==========

    /**
     * Creates a Base16 encoder with strict policy for testing.
     */
    private Base16 createStrictBase16Encoder() {
        return new Base16(false, CodecPolicy.STRICT);
    }

    /**
     * Creates a Base16 decoder with strict policy for testing.
     */
    private Base16 createStrictBase16Decoder() {
        return new Base16(false, CodecPolicy.STRICT);
    }

    /**
     * Creates a Base16 decoder with lenient policy for testing.
     */
    private Base16 createLenientBase16Decoder() {
        return new Base16(true, CodecPolicy.LENIENT);
    }

    /**
     * Creates a byte array with a negative value at index 1 for testing encoding.
     */
    private byte[] createByteArrayWithNegativeValue() {
        byte[] data = new byte[3];
        data[1] = NEGATIVE_BYTE_VALUE; // -23 (0xE9)
        return data;
    }
}