package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the isMetaphoneEqual method of the Metaphone class.
 */
@DisplayName("Metaphone isMetaphoneEqual")
class MetaphoneIsEqualTest extends AbstractStringEncoderTest<Metaphone> {

    @Override
    protected Metaphone createStringEncoder() {
        return new Metaphone();
    }

    /**
     * The original test file contained unused helper methods (assertMetaphoneEqual, validateFixture)
     * and a helper with redundant logic (assertIsMetaphoneEqual). These have been removed or
     * simplified to improve clarity and focus.
     */
    @Test
    @DisplayName("should return true for a group of phonetically similar words")
    void testWordsWithSameMetaphoneCodeAreConsideredEqual() {
        // Arrange: A group of words that are expected to have the same Metaphone code.
        final String sourceWord = "Paris";
        final String[] similarWords = {"Pearcy", "Perris", "Piercy", "Pierz", "Pryse"};
        final String expectedMetaphoneCode = "PRS"; // The shared Metaphone code.

        // For clarity, first verify the Metaphone code of the source word itself.
        // This makes the test's premise explicit and easier to debug.
        assertEquals(expectedMetaphoneCode, getStringEncoder().metaphone(sourceWord),
            "The Metaphone code for the source word '" + sourceWord + "' was not as expected.");

        // Act & Assert:
        // Use assertAll to ensure all comparisons are executed and all failures are reported together.
        // This is more informative than failing on the first mismatch.
        assertAll("All words in the group should be Metaphone-equal to the source word '" + sourceWord + "'",
            () -> {
                for (final String wordToCompare : similarWords) {
                    assertTrue(getStringEncoder().isMetaphoneEqual(sourceWord, wordToCompare),
                        () -> "Expected '" + sourceWord + "' to be phonetically equal to '" + wordToCompare + "', but it was not.");
                }
            }
        );
    }
}