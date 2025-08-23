package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for the static methods of {@link QuotedPrintableCodec}.
 * 
 * Note: The original test class "QuotedPrintableCodec_ESTestTest66" and its
 * scaffolding suggest it was auto-generated. This version is cleaned up
 * for better human comprehension.
 */
public class QuotedPrintableCodecStaticTest {

    /**
     * Tests that the static decodeQuotedPrintable method correctly handles a null input
     * by returning null, which is a common convention for safe API design.
     */
    @Test
    public void decodeQuotedPrintableWithNullInputShouldReturnNull() {
        // Arrange: The input is a null byte array.
        final byte[] inputBytes = null;

        // Act: Call the method under test with the null input.
        final byte[] result = QuotedPrintableCodec.decodeQuotedPrintable(inputBytes);

        // Assert: The method should return null.
        assertNull("Decoding a null byte array should result in null.", result);
    }
}