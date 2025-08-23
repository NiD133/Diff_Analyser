package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link Metaphone} class, focusing on its encoding behavior.
 */
public class MetaphoneTest {

    /**
     * Tests that the metaphone algorithm correctly handles an input string
     * containing non-alphabetic characters. The algorithm is expected to
     * strip these characters before processing, effectively ignoring them.
     */
    @Test
    public void metaphoneShouldIgnoreNonAlphabeticCharacters() {
        // Arrange: Set up the encoder and the test input.
        // The input string is intentionally "dirty" with numbers, punctuation,
        // and symbols to test the algorithm's preprocessing robustness.
        final Metaphone metaphone = new Metaphone();
        final String inputWithNonAlphabeticChars = "!1-HOe,>9Y[:a%E";

        // After stripping non-alphabetic characters and converting to uppercase,
        // the string becomes "HOEYAE". The Metaphone algorithm encodes this to "H".
        final String expectedCode = "H";

        // Act: Perform the encoding.
        final String actualCode = metaphone.metaphone(inputWithNonAlphabeticChars);

        // Assert: Verify that the generated code is correct.
        // This confirms that the non-alphabetic characters were successfully ignored
        // and the encoding proceeded as expected on the remaining letters.
        assertEquals(
            "The Metaphone encoding should ignore non-alphabetic characters",
            expectedCode,
            actualCode
        );
    }
}