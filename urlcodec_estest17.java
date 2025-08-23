package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the URLCodec class.
 */
public class URLCodecTest {

    /**
     * Tests that decoding a string that contains no URL-encoded characters
     * returns the original, unchanged string.
     *
     * This is a basic sanity check for the decode operation.
     */
    @Test
    public void decodeStringWithoutEncodedCharsReturnsOriginalString() throws Exception {
        // Arrange
        URLCodec urlCodec = new URLCodec();
        String inputString = "UTF-8"; // A simple string with no special characters to decode
        String expectedString = "UTF-8";

        // Act
        // The decode method is called with a specific charset. Since the input string
        // contains no encoded sequences (like %20 or +), the output should be identical.
        String actualString = urlCodec.decode(inputString, "UTF-8");

        // Assert
        assertEquals(expectedString, actualString);
    }
}