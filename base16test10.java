package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.codec.CodecPolicy;
import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.Test;

/**
 * Tests the constructors of the {@link Base16} class to ensure they correctly
 * configure the encoder and decoder's behavior.
 */
public class Base16ConstructorTest {

    private static final byte[] TEST_DATA = "Hello World".getBytes();
    private static final String ENCODED_UPPERCASE = "48656C6C6F20576F726C64";
    private static final String ENCODED_LOWERCASE = "48656c6c6f20576f726c64";

    @Test
    void defaultConstructorShouldEncodeToUpperCase() {
        // The default constructor should produce uppercase hexadecimal output.
        final Base16 base16 = new Base16();
        final String encoded = base16.encodeToString(TEST_DATA);
        assertEquals(ENCODED_UPPERCASE, encoded);
    }

    @Test
    void constructorWithFalseForLowerCaseShouldEncodeToUpperCase() {
        // The constructor with lowerCase=false should produce uppercase hexadecimal output.
        final Base16 base16 = new Base16(false);
        final String encoded = base16.encodeToString(TEST_DATA);
        assertEquals(ENCODED_UPPERCASE, encoded);
    }

    @Test
    void constructorWithTrueForLowerCaseShouldEncodeToLowerCase() {
        // The constructor with lowerCase=true should produce lowercase hexadecimal output.
        final Base16 base16 = new Base16(true);
        final String encoded = base16.encodeToString(TEST_DATA);
        assertEquals(ENCODED_LOWERCASE, encoded);
    }

    @Test
    void strictPolicyConstructorShouldThrowExceptionForOddLengthInput() {
        // The STRICT policy requires encoded input to have an even number of characters.
        // Decoding an odd-length string should fail.
        final Base16 base16 = new Base16(false, CodecPolicy.STRICT);
        final String oddLengthInput = "ABC";

        assertThrows(DecoderException.class, () -> {
            base16.decode(oddLengthInput);
        }, "Strict decoding should fail for input with an odd number of characters.");
    }

    @Test
    void lenientPolicyConstructorShouldIgnoreTrailingCharacterForOddLengthInput() throws DecoderException {
        // The LENIENT policy should ignore a single trailing character that cannot form a complete byte.
        // "ABC" -> "AB" should be decoded, "C" should be ignored.
        final Base16 base16 = new Base16(false, CodecPolicy.LENIENT);
        final String oddLengthInput = "ABC";
        final byte[] expected = {(byte) 0xAB};

        final byte[] decoded = base16.decode(oddLengthInput);

        assertArrayEquals(expected, decoded);
    }
}