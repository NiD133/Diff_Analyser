package org.apache.commons.codec.net;

import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

/**
 * Tests for the {@link BCodec} class.
 */
public class BCodecTest {

    /**
     * Tests that {@link BCodec#encode(String)} returns null when the input string is null.
     * This behavior is consistent with the contract of the {@link org.apache.commons.codec.StringEncoder} interface.
     */
    @Test
    public void encodeStringShouldReturnNullForNullInput() throws EncoderException {
        // Arrange
        final BCodec bCodec = new BCodec();

        // Act
        final String result = bCodec.encode(null);

        // Assert
        assertNull("The result of encoding a null string should be null.", result);
    }
}