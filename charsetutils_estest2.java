package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link CharSetUtils}.
 */
public class CharSetUtilsTest {

    @Test
    // The test method name clearly describes the behavior being tested:
    // what the method does (squeezes repeated characters) and under what condition
    // (when the characters are in the provided set).
    public void squeeze_shouldSqueezeRepeatedCharacters_whenTheyAreInTheProvidedSet() {
        // --- Arrange ---
        // Use clear, descriptive variable names and simple, focused test data.
        // The input string contains repeated characters:
        // - 'll' and 'oo' (which are in the set to be squeezed)
        // - 'pp' (which is NOT in the set)
        final String input = "ballooning applications";

        // The expected output after squeezing 'll' to 'l' and 'oo' to 'o'.
        // The repeated 'pp' in "applications" should remain unchanged because 'p' is not in the set.
        final String expected = "baloning applications";

        // --- Act ---
        // Call the method under test. Using varargs for the character set is cleaner
        // than creating an explicit array.
        final String actual = CharSetUtils.squeeze(input, "l", "o");

        // --- Assert ---
        // Verify that the actual result matches the expected outcome.
        // An assertion message is added for clarity in case the test fails.
        assertEquals(
            "Squeezing 'l' and 'o' should reduce 'll' and 'oo' to single characters.",
            expected,
            actual
        );
    }
}