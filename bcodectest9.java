package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link BCodec} class.
 *
 * <p>This version has been refactored for improved clarity and maintainability.</p>
 */
public class BCodecTest {

    // Note: Unused constants (BASE64_IMPOSSIBLE_CASES, etc.) and the helper method
    // (constructString) from the original class were removed to reduce clutter and
    // improve focus on the test's intent.

    @Test
    void encodeShouldReturnNullWhenInputStringIsNull() throws EncoderException {
        // Arrange
        final BCodec bCodec = new BCodec();
        // The charset name is required by the method signature but is not used when the input is null.
        // A standard charset name is used here for clarity and correctness.
        final String charsetName = "UTF-8";

        // Act
        final String result = bCodec.encode(null, charsetName);

        // Assert
        assertNull(result, "The result of encoding a null string should be null.");
    }
}