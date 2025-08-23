package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link BCodec}.
 */
public class BCodecTest {

    @Test
    public void decodeShouldThrowDecoderExceptionForMalformedInput() {
        // Arrange: Create a BCodec instance and define an input string that
        // does not conform to the RFC 1522 encoded-word format "=?charset?encoding?encoded-text?=".
        final BCodec bCodec = new BCodec();
        final String malformedInput = "An invalid encoded string";

        // Act & Assert: Verify that attempting to decode the malformed string
        // throws a DecoderException with the expected message.
        final DecoderException exception = assertThrows(
            DecoderException.class,
            () -> bCodec.decode(malformedInput)
        );

        assertEquals("RFC 1522 violation: malformed encoded content", exception.getMessage());
    }
}