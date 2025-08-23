package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link PercentCodec} class.
 */
public class PercentCodecTest {

    /**
     * Tests that the encode method follows the common convention of returning
     * null when given a null input.
     */
    @Test
    public void encodeShouldReturnNullForNullInput() {
        // Arrange
        final PercentCodec percentCodec = new PercentCodec();

        // Act
        final byte[] result = percentCodec.encode(null);

        // Assert
        assertNull("Encoding a null byte array should result in null.", result);
    }
}