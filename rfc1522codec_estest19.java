package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for the RFC1522Codec class, focusing on the handling of malformed input.
 */
public class RFC1522CodecTest {

    /**
     * Tests that the decodeText method correctly throws a DecoderException
     * when given an empty string, which is not a valid RFC 1522 encoded-word.
     */
    @Test
    public void decodeTextShouldThrowDecoderExceptionForEmptyInput() {
        // Arrange: An RFC 1522 encoded-word cannot be empty.
        // We use BCodec as a concrete implementation to test the abstract RFC1522Codec's method.
        final BCodec codec = new BCodec();
        final String malformedInput = "";

        // Act & Assert: Verify that decoding the malformed input throws the expected exception.
        final DecoderException thrown = assertThrows(
                "Decoding an empty string should fail.",
                DecoderException.class,
                () -> codec.decodeText(malformedInput)
        );

        // Assert on the exception message for more specific verification.
        assertEquals(
                "RFC 1522 violation: malformed encoded content",
                thrown.getMessage()
        );
    }
}