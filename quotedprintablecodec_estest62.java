package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Contains tests for the static methods of the {@link QuotedPrintableCodec} class.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that the static {@code encodeQuotedPrintable} method correctly handles a null
     * input byte array by returning null. The behavior is based on the method's
     * implementation, which performs a null check on the byte array argument.
     */
    @Test
    public void encodeQuotedPrintableShouldReturnNullForNullByteArray() {
        // Arrange: Define a null byte array to be encoded.
        // The BitSet argument can also be null, as the byte array is checked first.
        byte[] nullInput = null;

        // Act: Call the static encode method with the null input.
        byte[] result = QuotedPrintableCodec.encodeQuotedPrintable(null, nullInput);

        // Assert: Verify that the method returns null as expected.
        assertNull("Encoding a null byte array should return null.", result);
    }
}