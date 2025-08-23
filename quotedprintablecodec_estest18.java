package org.apache.commons.codec.net;

import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

// Note: The original test extended an EvoSuite scaffolding class.
// This has been removed to make the test more self-contained and readable.
public class QuotedPrintableCodec_ESTestTest18 {

    /**
     * Tests that decoding and then re-encoding an empty byte array
     * correctly results in an empty byte array. This verifies the codec's
     * behavior for a common edge case.
     */
    @Test
    public void testDecodeThenEncodeOnEmptyByteArrayProducesEmptyArray() throws Exception {
        // Arrange
        // Use a specific charset for test consistency, avoiding platform-dependent behavior.
        final Charset charset = StandardCharsets.UTF_8;
        final QuotedPrintableCodec codec = new QuotedPrintableCodec(charset);
        final byte[] emptyInput = new byte[0];

        // Act
        // First, decode the empty input.
        final byte[] decodedBytes = codec.decode(emptyInput);
        // Then, encode the result of the decoding.
        final byte[] finalResult = codec.encode(decodedBytes);

        // Assert
        // The intermediate decoded array should be empty.
        assertNotNull("Decoding should not result in a null array", decodedBytes);
        assertEquals("Decoding an empty array should produce an empty array", 0, decodedBytes.length);

        // The final re-encoded array should be content-equal to the original empty array.
        assertNotNull("Encoding should not result in a null array", finalResult);
        assertArrayEquals("Encoding an empty array should produce an empty array", emptyInput, finalResult);

        // The original test checked for instance inequality, which ensures a new array is
        // created rather than returning the input object. We'll keep this check.
        assertNotSame("Encoding should produce a new array instance", decodedBytes, finalResult);
    }
}