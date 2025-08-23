package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link PercentCodec}.
 */
// The original class name "PercentCodecTestTest9" was likely auto-generated or a typo.
// Renaming it to "PercentCodecTest" follows standard Java testing conventions.
public class PercentCodecTest {

    /**
     * Tests that the PercentCodec constructor throws an IllegalArgumentException if the
     * 'alwaysEncodeChars' array contains a non-US-ASCII byte. The constructor's contract
     * requires these characters to be within the US-ASCII range (0-127).
     */
    @Test
    // The original name "testInvalidByte" was too generic. This name clearly states
    // the specific behavior being tested and the expected outcome.
    void constructorShouldThrowExceptionForNonAsciiByteInAlwaysEncodeList() {
        // Arrange: Create a byte array containing a non-US-ASCII value (-1).
        // The 'alwaysEncodeChars' parameter is expected to contain only US-ASCII bytes.
        final byte[] invalidChars = { (byte) -1, (byte) 'A' };

        // Act & Assert: Verify that instantiating the codec with the invalid array
        // throws an IllegalArgumentException.
        assertThrows(IllegalArgumentException.class, () -> {
            new PercentCodec(invalidChars, true);
        });
    }
}