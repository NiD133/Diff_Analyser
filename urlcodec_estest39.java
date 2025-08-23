package org.apache.commons.codec.net;

import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Test case for the URLCodec class, focusing on its decoding behavior.
 */
public class URLCodecTest {

    /**
     * Tests that the decode(String, String) method correctly handles a null input
     * by returning null. The method should not attempt to perform any decoding
     * and should return immediately.
     */
    @Test
    public void decodeWithNullStringShouldReturnNull() throws Exception {
        // Arrange
        URLCodec urlCodec = new URLCodec(); // Use default constructor for clarity
        String input = null;

        // Act
        // The charset is irrelevant here, as the method should return early for null input.
        // We use a standard charset to make the test clearer.
        String result = urlCodec.decode(input, "UTF-8");

        // Assert
        assertNull("Decoding a null string should result in a null value.", result);
    }
}