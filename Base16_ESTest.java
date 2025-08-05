package org.apache.commons.codec.binary;

import org.apache.commons.codec.CodecPolicy;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * Provides well-documented tests for the {@link Base16} class, focusing on readability and maintainability.
 */
public class Base16Test {

    private static final byte[] PLAIN_TEXT_BYTES = "Hello World".getBytes(StandardCharsets.UTF_8);
    private static final String ENCODED_UPPERCASE = "48656C6C6F20576F726C64";
    private static final String ENCODED_LOWERCASE = "48656c6c6f20576f726c64";

    // =================================================================
    // Constructor Tests
    // =================================================================

    @Test
    public void defaultConstructorShouldCreateUpperCaseEncoder() {
        final Base16 base16 = new Base16();
        assertEquals("Default constructor should produce uppercase output.",
                ENCODED_UPPERCASE, base16.encodeToString(PLAIN_TEXT_BYTES));
    }

    @Test
    public void constructorShouldCreateLowerCaseEncoderWhenSpecified() {
        final Base16 base16LowerCase = new Base16(true); // true for lowercase
        assertEquals("Constructor with 'true' should produce lowercase output.",
                ENCODED_LOWERCASE, base16LowerCase.encodeToString(PLAIN_TEXT_BYTES));
    }

    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowNullPointerExceptionForNullCodecPolicy() {
        new Base16(false, null);
    }

    // =================================================================
    // Encoding Tests
    // =================================================================

    @Test
    public void encodeShouldProduceCorrectUpperCaseString() {
        final Base16 base16 = new Base16(false); // false for uppercase
        final String encoded = base16.encodeToString(PLAIN_TEXT_BYTES);
        assertEquals("Encoding 'Hello World' should produce correct uppercase hex string.",
                ENCODED_UPPERCASE, encoded);
    }

    @Test
    public void encodeShouldHandleBytesWithNegativeValues() {
        final Base16 base16 = new Base16(); // Defaults to uppercase
        final byte[] input = {0x00, (byte) 0xE9, 0x00}; // 0xE9 is -23
        final byte[] expected = "00E900".getBytes(StandardCharsets.US_ASCII);

        final byte[] actual = base16.encode(input);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void encodeShouldReturnEmptyArrayForEmptyInput() {
        final Base16 base16 = new Base16();
        final byte[] encoded = base16.encode(new byte[0]);
        assertArrayEquals(new byte[0], encoded);
    }

    @Test
    public void encodeShouldReturnEmptyArrayForNegativeLength() {
        final Base16 base16 = new Base16();
        final byte[] result = base16.encode(new byte[5], -1, -1);
        assertArrayEquals("Encoding with negative length should result in an empty array.", new byte[0], result);
    }

    // =================================================================
    // Decoding Tests
    // =================================================================

    @Test
    public void decodeShouldCorrectlyDecodeUpperCaseString() {
        final Base16 base16 = new Base16();
        final byte[] decoded = base16.decode(ENCODED_UPPERCASE);
        assertArrayEquals("Decoding an uppercase hex string should yield the original bytes.",
                PLAIN_TEXT_BYTES, decoded);
    }

    @Test
    public void decodeShouldCorrectlyDecodeLowerCaseString() {
        final Base16 base16 = new Base16(); // Default decoder handles both cases
        final byte[] decoded = base16.decode(ENCODED_LOWERCASE);
        assertArrayEquals("Decoding a lowercase hex string should yield the original bytes.",
                PLAIN_TEXT_BYTES, decoded);
    }

    @Test
    public void decodeShouldReturnEmptyArrayForEmptyString() {
        final Base16 base16 = new Base16();
        final byte[] decoded = base16.decode("");
        assertArrayEquals(new byte[0], decoded);
    }

    @Test(expected = IllegalArgumentException.class)
    public void decodeShouldThrowExceptionForInvalidCharacter() {
        final Base16 base16 = new Base16();
        // 'G' is not a valid hex character.
        base16.decode("48656C6C6F20576F726C64G");
    }

    @Test(expected = IllegalArgumentException.class)
    public void decodeWithStrictPolicyShouldThrowExceptionForOddLengthString() {
        final Base16 base16 = new Base16(false, CodecPolicy.STRICT);
        // A single character is not a complete byte representation.
        base16.decode("A");
    }

    @Test
    public void decodeWithLenientPolicyShouldIgnoreTrailingCharacter() {
        final Base16 base16 = new Base16(false, CodecPolicy.LENIENT);
        // Lenient policy should ignore the trailing 'C'. "ABC" -> "AB" -> {0xAB}
        final byte[] decoded = base16.decode("ABC");
        assertArrayEquals(new byte[]{(byte) 0xAB}, decoded);
    }

    // =================================================================
    // isInAlphabet Tests
    // =================================================================

    @Test
    public void isInAlphabetShouldReturnTrueForValidCharacters() {
        final Base16 base16 = new Base16(); // Uppercase codec
        assertTrue("'0' should be in the alphabet", base16.isInAlphabet((byte) '0'));
        assertTrue("'9' should be in the alphabet", base16.isInAlphabet((byte) '9'));
        assertTrue("'A' should be in the alphabet", base16.isInAlphabet((byte) 'A'));
        assertTrue("'F' should be in the alphabet", base16.isInAlphabet((byte) 'F'));
    }

    @Test
    public void isInAlphabetShouldReturnFalseForInvalidCharacters() {
        final Base16 base16 = new Base16(); // Uppercase codec
        assertFalse("'G' should not be in the alphabet", base16.isInAlphabet((byte) 'G'));
        assertFalse("'a' should not be in the alphabet for an uppercase codec", base16.isInAlphabet((byte) 'a'));
        assertFalse("' ' should not be in the alphabet", base16.isInAlphabet((byte) ' '));
    }

    @Test
    public void isInAlphabetShouldBeCaseSensitiveBasedOnCodec() {
        final Base16 base16LowerCase = new Base16(true); // Lowercase codec
        assertTrue("'a' should be in the alphabet for a lowercase codec", base16LowerCase.isInAlphabet((byte) 'a'));
        assertFalse("'A' should NOT be in the alphabet for a lowercase codec", base16LowerCase.isInAlphabet((byte) 'A'));
    }

    // =================================================================
    // Streaming API and Edge Case Tests
    // =================================================================

    @Test(expected = IllegalArgumentException.class)
    public void decodeStreamingShouldThrowExceptionForInvalidByte() {
        final Base16 base16 = new Base16();
        final BaseNCodec.Context context = new BaseNCodec.Context();
        // The byte value 0 is not a valid Base16 character (the character '0' is ASCII 48).
        final byte[] invalidInput = new byte[]{0};
        base16.decode(invalidInput, 0, invalidInput.length, context);
    }

    @Test(expected = NullPointerException.class)
    public void encodeStreamingShouldThrowNullPointerExceptionForNullInput() {
        final Base16 base16 = new Base16();
        base16.encode(null, 0, 0, new BaseNCodec.Context());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void encodeStreamingShouldThrowBoundsExceptionForInvalidOffset() {
        final Base16 base16 = new Base16();
        base16.encode(new byte[10], 10, 1, new BaseNCodec.Context());
    }

    @Test(expected = NullPointerException.class)
    public void decodeStreamingShouldThrowNullPointerExceptionForNullInput() {
        final Base16 base16 = new Base16();
        base16.decode(null, 0, 0, new BaseNCodec.Context());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void decodeStreamingShouldThrowBoundsExceptionForInvalidOffset() {
        final Base16 base16 = new Base16();
        // Attempt to access index 1 of a 1-byte array.
        base16.decode(new byte[1], 1, 1, new BaseNCodec.Context());
    }
}