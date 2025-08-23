package org.apache.commons.codec.net;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for the {@link PercentCodec} class.
 */
public class PercentCodecTest {

    @Test
    public void encodeShouldReturnNullWhenGivenNullObject() throws EncoderException {
        // Arrange
        PercentCodec percentCodec = new PercentCodec();
        Object nullInput = null;

        // Act
        Object result = percentCodec.encode(nullInput);

        // Assert
        assertNull("Encoding a null object should return null.", result);
    }
}