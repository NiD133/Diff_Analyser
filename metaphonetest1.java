package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests for the {@link Metaphone} phonetic encoding algorithm.
 */
public class MetaphoneTest {

    private final Metaphone metaphoneEncoder = new Metaphone();

    /**
     * Tests that the Metaphone algorithm correctly handles special 'SC' combinations.
     * According to the Metaphone rules, 'SC' followed by 'I', 'E', or 'Y' often
     * produces an 'S' sound and is encoded differently than other 'SC' combinations.
     */
    @ParameterizedTest(name = "Metaphone for \"{0}\" should be \"{1}\"")
    @CsvSource({
        "SCIENCE, SNS",
        "SCENE,   SN",
        "SCY,     S"
    })
    void metaphoneShouldHandleSpecialSCCombinations(final String input, final String expected) {
        assertEquals(expected, this.metaphoneEncoder.metaphone(input));
    }

    // --- Test Helper Methods ---
    // The following methods are general-purpose helpers, likely used in a larger test suite
    // for validating groups or pairs of words that should be phonetically equivalent.

    /**
     * Asserts that a source string and an array of other strings are all metaphonically equal.
     * This is useful for testing a group of words that should all resolve to the same code.
     *
     * @param source  The reference string.
     * @param matches An array of strings to compare against the source.
     */
    public void assertMetaphoneGroupIsEqual(final String source, final String[] matches) {
        assertNotNull(matches, "Matches array cannot be null.");
        for (final String match : matches) {
            assertTrue(this.metaphoneEncoder.isMetaphoneEqual(source, match),
                () -> String.format("Expected '%s' and '%s' to have the same Metaphone encoding.", source, match));
        }
    }

    /**
     * Asserts that for each pair of strings, their Metaphone encodings are equal.
     * This method checks for symmetry by testing both isMetaphoneEqual(a, b) and isMetaphoneEqual(b, a).
     *
     * @param pairs A 2D array where each inner array is a pair of strings expected to be metaphonically equal.
     */
    public void assertMetaphonePairsAreEqual(final String[][] pairs) {
        validatePairsFixture(pairs);
        for (final String[] pair : pairs) {
            final String name1 = pair[0];
            final String name2 = pair[1];
            assertTrue(this.metaphoneEncoder.isMetaphoneEqual(name1, name2),
                () -> String.format("Expected Metaphone match between '%s' and '%s'", name1, name2));
            // Also check the symmetric case
            assertTrue(this.metaphoneEncoder.isMetaphoneEqual(name2, name1),
                () -> String.format("Symmetric check failed: Expected Metaphone match between '%s' and '%s'", name2, name1));
        }
    }

    /**
     * Validates that the fixture for pair-based testing is correctly structured.
     *
     * @param pairs The fixture to validate.
     */
    private void validatePairsFixture(final String[][] pairs) {
        assertNotNull(pairs, "Test fixture cannot be null.");
        if (pairs.length == 0) {
            fail("Test fixture cannot be empty.");
        }
        for (int i = 0; i < pairs.length; i++) {
            if (pairs[i] == null || pairs[i].length != 2) {
                fail("Fixture error: array at index " + i + " must contain exactly 2 strings.");
            }
        }
    }
}