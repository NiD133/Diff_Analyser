package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Unit tests for the {@link URLCodec} class.
 */
public class URLCodecTest {

    /**
     * Tests that encoding an empty byte array correctly results in an empty byte array.
     * This verifies the handling of an important edge case.
     */
    @Test
    public void encodeEmptyByteArrayShouldReturnEmptyByteArray() {
        // Arrange: Create a URLCodec instance and the input data.
        final URLCodec urlCodec = new URLCodec();
        final byte[] emptyInput = new byte[0];

        // Act: Call the method under test.
        final byte[] result = urlCodec.encode(emptyInput);

        // Assert: Verify that the output is also an empty byte array.
        final byte[] expectedOutput = new byte[0];
        assertArrayEquals(expectedOutput, result);
    }
}