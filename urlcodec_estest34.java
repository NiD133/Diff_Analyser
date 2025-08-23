package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Contains tests for the {@link URLCodec} class.
 */
public class URLCodecTest {

    /**
     * Tests that the encode(String, String) method returns null when the input string is null.
     * The behavior of encoders on null input is a critical edge case to verify.
     */
    @Test
    public void testEncodeStringWithCharsetGivenNullStringReturnsNull() {
        // Arrange
        URLCodec urlCodec = new URLCodec();
        String inputStringToEncode = null;

        // Act
        // The charset argument is irrelevant when the input string is null,
        // but we pass null to match the original test's behavior.
        String encodedResult = urlCodec.encode(inputStringToEncode, null);

        // Assert
        assertNull("Encoding a null string should return null.", encodedResult);
    }
}