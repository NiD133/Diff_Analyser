package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Test cases for the {@link PercentCodec} class.
 */
public class PercentCodecTest {

    /**
     * Verifies that decoding an empty byte array results in an empty byte array.
     * This test covers the edge case of handling empty input.
     */
    @Test
    public void decodeEmptyByteArrayShouldReturnEmptyByteArray() throws DecoderException {
        // Arrange: Set up the test conditions.
        final PercentCodec codec = new PercentCodec();
        final byte[] emptyInput = new byte[0];

        // Act: Call the method under test.
        final byte[] decodedResult = codec.decode(emptyInput);

        // Assert: Verify the outcome is as expected.
        final byte[] expectedResult = new byte[0];
        assertArrayEquals("Decoding an empty array should produce an empty array.", expectedResult, decodedResult);
    }
}