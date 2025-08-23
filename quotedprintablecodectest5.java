package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.Test;

/**
 * Tests for the static {@link QuotedPrintableCodec#decodeQuotedPrintable(byte[])} method.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that decoding a null byte array returns null, which is the expected behavior
     * for this edge case.
     */
    @Test
    void decodeQuotedPrintableWithNullInputShouldReturnNull() throws DecoderException {
        // Arrange: A null byte array is the input for this test case.
        final byte[] input = null;

        // Act: Call the static decode method with the null input.
        final byte[] result = QuotedPrintableCodec.decodeQuotedPrintable(input);

        // Assert: Verify that the result is null, as per the method's contract for null inputs.
        assertNull(result, "Decoding a null byte array should return null.");
    }
}