package org.apache.commons.codec.net;

import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * Provides tests for the {@link BCodec} class.
 */
public class BCodecTest {

    /**
     * Tests that the encode method returns null when the input string is null,
     * as specified by the method's contract.
     */
    @Test
    public void encodeShouldReturnNullWhenInputStringIsNull() {
        // Arrange
        BCodec bCodec = new BCodec();
        String nullString = null;
        // The charset is irrelevant in this case, but we use a null charset name
        // to match the original test's scenario.
        String charsetName = null;

        // Act
        // The BCodec class has two overloaded encode methods:
        // 1. encode(String, String)
        // 2. encode(String, Charset)
        // Using a typed variable for the null charset avoids ambiguity.
        String result = bCodec.encode(nullString, charsetName);

        // Assert
        assertNull("Encoding a null string should return null.", result);
    }
}