package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * Unit tests for the {@link QuotedPrintableCodec} class.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that decoding an empty byte array results in a new, empty byte array.
     */
    @Test
    public void decodeQuotedPrintableWithEmptyArrayReturnsEmptyArray() throws DecoderException {
        // Arrange: Create an empty byte array as input.
        final byte[] emptyInput = new byte[0];
        final byte[] expectedOutput = new byte[0];

        // Act: Decode the empty byte array.
        final byte[] actualOutput = QuotedPrintableCodec.decodeQuotedPrintable(emptyInput);

        // Assert: Verify that the result is an empty byte array.
        assertArrayEquals("Decoding an empty array should produce an empty array.", expectedOutput, actualOutput);
    }
}