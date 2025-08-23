package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link URLCodec} class.
 */
public class URLCodecTest {

    /**
     * Tests that encoding a null String returns null.
     *
     * The {@link org.apache.commons.codec.StringEncoder#encode(String)} method contract
     * implies that a null input should result in a null output. This test verifies
     * that {@link URLCodec} adheres to this convention.
     */
    @Test
    public void encodeStringShouldReturnNullWhenInputIsNull() {
        // Arrange
        URLCodec urlCodec = new URLCodec();

        // Act
        String result = urlCodec.encode(null);

        // Assert
        assertNull("The result of encoding a null string should be null.", result);
    }
}