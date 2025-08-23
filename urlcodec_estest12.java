package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link URLCodec} class.
 */
public class URLCodecTest {

    /**
     * Tests that encoding an empty string correctly results in an empty string.
     */
    @Test
    public void encodeEmptyStringShouldReturnEmptyString() throws Exception {
        // Arrange
        final URLCodec urlCodec = new URLCodec();
        final String input = "";
        final String expectedOutput = "";

        // Act
        final String actualOutput = urlCodec.encode(input, "UTF-8");

        // Assert
        assertEquals(expectedOutput, actualOutput);
    }
}