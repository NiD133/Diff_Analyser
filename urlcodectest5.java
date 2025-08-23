package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertNull;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link URLCodec}.
 */
// The class name has been simplified from "URLCodecTestTest5" to "URLCodecTest"
// to follow standard naming conventions and improve readability.
class URLCodecTest {

    // The unused static fields (SWISS_GERMAN_STUFF_UNICODE, RUSSIAN_STUFF_UNICODE)
    // and helper methods (constructString, validateState) have been removed.
    // They were not used by any test, adding clutter and making the code harder
    // to understand. A test class should only contain code relevant to its tests.

    @Test
    // The test method name now clearly describes the scenario and expected outcome.
    // The unnecessary "throws Exception" clause was removed, as the code path for a
    // null input is not expected to throw any exceptions.
    void decodeWithNullStringShouldReturnNull() {
        // Arrange
        final URLCodec urlCodec = new URLCodec();
        final String nullInput = null;

        // Act
        // The decode method returns null immediately if the input string is null.
        // We pass a valid charset name for correctness, although it is not used in this case.
        final String result = urlCodec.decode(nullInput, StandardCharsets.UTF_8.name());

        // Assert
        assertNull(result, "Decoding a null string should return null.");
    }
}