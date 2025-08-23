package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link PercentCodec} class, focusing on invalid decoding scenarios.
 */
public class PercentCodecTest {

    /**
     * Tests that the decode method throws a DecoderException when the input byte array
     * ends with a single '%' character. A '%' escape character must be followed by
     * two hexadecimal digits to be valid.
     */
    @Test
    public void decodeShouldThrowDecoderExceptionForInputWithTrailingPercentSign() {
        // Arrange
        final PercentCodec percentCodec = new PercentCodec();
        final byte[] inputWithTrailingPercent = {'a', 'b', 'c', '%'};

        // Act & Assert
        try {
            percentCodec.decode(inputWithTrailingPercent);
            fail("Expected a DecoderException to be thrown for an incomplete escape sequence.");
        } catch (final DecoderException e) {
            // Verify that the correct exception was thrown with a meaningful message.
            final String expectedMessage = "Invalid percent decoding: incomplete escape sequence";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}