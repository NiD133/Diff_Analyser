package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link BCodec} class.
 */
public class BCodecTest {

    /**
     * Tests that the doEncoding method handles null input gracefully by returning null.
     * This is a common convention for "null-safe" methods.
     */
    @Test
    public void doEncodingShouldReturnNullForNullInput() {
        // Arrange
        BCodec codec = new BCodec();
        byte[] nullInput = null;

        // Act
        byte[] result = codec.doEncoding(nullInput);

        // Assert
        assertNull("Encoding a null byte array should return null.", result);
    }
}