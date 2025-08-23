package org.apache.commons.codec.net;

import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * Unit tests for the QuotedPrintableCodec class.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that encoding a null byte array returns null, which is the expected
     * behavior for encoders in this library when handling null input.
     */
    @Test
    public void encodeByteArrayShouldReturnNullForNullInput() {
        // Arrange: Create a codec instance. The specific charset doesn't matter
        // for this test case, so the default constructor is used.
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // Act: Call the encode method with a null input.
        final byte[] result = codec.encode(null);

        // Assert: Verify that the output is null.
        assertNull("Encoding a null byte array should return null.", result);
    }
}