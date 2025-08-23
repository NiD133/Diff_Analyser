package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link Metaphone} encoder.
 */
public class MetaphoneTest extends AbstractStringEncoderTest<Metaphone> {

    @Override
    protected Metaphone createStringEncoder() {
        return new Metaphone();
    }

    @Test
    @DisplayName("The 'CIA' sequence in a word should be encoded as 'X'")
    void shouldEncodeCiaAsX() {
        // This test case verifies the special encoding rule for "CIA".
        // Metaphone("CIAPO") -> "X" (from CIA) + "P" (from P) = "XP"
        assertEquals("XP", getStringEncoder().metaphone("CIAPO"));
    }

    // region Helper Methods for Data-Driven Tests
    /*
     * The following methods are general-purpose helpers for creating data-driven
     * tests for the isMetaphoneEqual method. They are not used by the specific
     * test case above but are kept for broader test suite consistency.
     */

    /**
     * Asserts that a source string and an array of matching strings all have the same Metaphone encoding.
     * <p>
     * This method performs two checks:
     * <ol>
     *     <li>The source string is compared against every string in the matches array.</li>
     *     <li>Every string in the matches array is compared against every other string in the array (all-pairs).</li>
     * </ol>
     *
     * @param source  The primary string to check.
     * @param matches An array of strings that should have the same Metaphone value as the source.
     */
    public void assertMetaphoneIsEqualForAllInGroup(final String source, final String[] matches) {
        final Metaphone metaphone = getStringEncoder();

        // 1. Match source to all matches
        for (final String match : matches) {
            assertTrue(metaphone.isMetaphoneEqual(source, match),
                () -> "Source: " + source + ", should have same Metaphone as: " + match);
        }

        // 2. Match all pairs within the matches array
        for (final String match1 : matches) {
            for (final String match2 : matches) {
                assertTrue(metaphone.isMetaphoneEqual(match1, match2),
                    () -> "Match '" + match1 + "' should be equal to '" + match2 + "'");
            }
        }
    }

    /**
     * Asserts that for each pair of strings in the input array, their Metaphone encodings are equal.
     *
     * @param pairs An array of string pairs, where each pair is expected to have the same Metaphone value.
     */
    public void assertMetaphoneIsEqualForPairs(final String[][] pairs) {
        validatePairFixture(pairs);
        final Metaphone metaphone = getStringEncoder();
        for (final String[] pair : pairs) {
            final String name0 = pair[0];
            final String name1 = pair[1];
            final String failMsg = "Expected Metaphone match between '" + name0 + "' and '" + name1 + "'";

            assertTrue(metaphone.isMetaphoneEqual(name0, name1), failMsg);
            assertTrue(metaphone.isMetaphoneEqual(name1, name0), failMsg);
        }
    }

    /**
     * Validates that the test fixture for pair-based testing is not empty and that each entry is a valid pair.
     *
     * @param pairs The fixture to validate.
     * @throws AssertionError if the fixture is invalid.
     */
    private void validatePairFixture(final String[][] pairs) {
        assertNotNull(pairs, "Test fixture (pairs) cannot be null.");
        if (pairs.length == 0) {
            fail("Test fixture is empty.");
        }
        for (int i = 0; i < pairs.length; i++) {
            if (pairs[i].length != 2) {
                fail("Invalid pair at index " + i + ": Fixture entries must contain exactly two strings.");
            }
        }
    }
    // endregion
}