package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link Metaphone} encoder.
 */
class MetaphoneTest extends AbstractStringEncoderTest<Metaphone> {

    @Override
    protected Metaphone createStringEncoder() {
        return new Metaphone();
    }

    /**
     * Tests that words starting with 'W' or 'WH' can be phonetically equivalent.
     * The Metaphone algorithm treats initial 'W' and 'WH' similarly.
     *
     * <p>Reference data was computed from
     * <a href="http://www.lanw.com/java/phonetic/default.htm">an online Metaphone calculator</a>.
     * </p>
     */
    @Test
    void testIsMetaphoneEqualForWordsStartingWithW() {
        assertHaveSameMetaphoneEncoding(
            "White",
            "Wade", "Wait", "Waite", "Wat", "Whit", "Wiatt", "Wit", "Wittie", "Witty",
            "Wood", "Woodie", "Woody"
        );
    }

    //--- Helper Methods for this and other tests in the suite ---

    /**
     * Asserts that a group of strings all have the same Metaphone encoding as a given source string.
     * <p>
     * This method is more efficient than a pair-wise comparison and provides more detailed
     * failure messages by comparing the actual encoded values.
     * </p>
     *
     * @param source  The string to use as the reference for the encoding.
     * @param matches A variable number of strings to compare against the source.
     */
    private void assertHaveSameMetaphoneEncoding(final String source, final String... matches) {
        final String expectedEncoding = getStringEncoder().metaphone(source);

        for (final String match : matches) {
            final String actualEncoding = getStringEncoder().metaphone(match);
            assertEquals(expectedEncoding, actualEncoding,
                () -> String.format(
                    "Metaphone encoding for '%s' should be '%s', but was '%s'. Mismatch with source '%s'.",
                    match, expectedEncoding, actualEncoding, source));
        }
    }

    /**
     * Asserts that pairs of strings are considered phonetically equivalent by {@link Metaphone#isMetaphoneEqual}.
     *
     * @param pairs A 2D array where each inner array contains two strings that should be phonetically equal.
     */
    public void assertMetaphoneEqual(final String[][] pairs) {
        validateFixture(pairs);
        for (final String[] pair : pairs) {
            final String name0 = pair[0];
            final String name1 = pair[1];
            final String failMsg = "Expected Metaphone match between \"" + name0 + "\" and \"" + name1 + "\"";

            // The isMetaphoneEqual method should be symmetric, so one check is sufficient.
            assertTrue(getStringEncoder().isMetaphoneEqual(name0, name1), failMsg);
        }
    }

    /**
     * Validates that the fixture for {@link #assertMetaphoneEqual} is structured correctly.
     *
     * @param pairs The fixture to validate.
     */
    public void validateFixture(final String[][] pairs) {
        if (pairs.length == 0) {
            fail("Test fixture must not be empty.");
        }
        for (int i = 0; i < pairs.length; i++) {
            if (pairs[i].length != 2) {
                fail("Fixture error: array at index " + i + " must have exactly 2 elements.");
            }
        }
    }
}