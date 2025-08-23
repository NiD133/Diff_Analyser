package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link Metaphone#isMetaphoneEqual(String, String)} method.
 */
class MetaphoneTest extends AbstractStringEncoderTest<Metaphone> {

    @Override
    protected Metaphone createStringEncoder() {
        return new Metaphone();
    }

    /**
     * Asserts that a reference string and a series of candidate strings all share the same Metaphone encoding.
     * This is a more efficient and direct way to test group phonetic equality than the original implementation.
     *
     * @param reference  The string to compare against.
     * @param candidates An array of strings that should be phonetically equal to the reference.
     */
    private void assertMetaphoneEquals(final String reference, final String[] candidates) {
        // Pre-calculate the expected Metaphone code from the reference string.
        final String expectedMetaphone = getStringEncoder().metaphone(reference);

        // Verify that all candidate strings produce the same Metaphone code.
        for (final String candidate : candidates) {
            assertEquals(expectedMetaphone, getStringEncoder().metaphone(candidate),
                () -> "Expected '" + candidate + "' to have the same Metaphone code as '" + reference + "'");
        }
    }

    /**
     * This is a utility method for test cases that are structured as pairs of matching strings.
     * It remains public as it might be used by other tests in a larger suite.
     *
     * @param pairs A 2D array where each inner array contains two strings that should be Metaphone-equal.
     */
    public void assertMetaphoneEqual(final String[][] pairs) {
        validateFixture(pairs);
        for (final String[] pair : pairs) {
            final String name0 = pair[0];
            final String name1 = pair[1];
            final String failMsg = "Expected match between " + name0 + " and " + name1;
            // isMetaphoneEqual should be commutative.
            assertTrue(getStringEncoder().isMetaphoneEqual(name0, name1), failMsg);
            assertTrue(getStringEncoder().isMetaphoneEqual(name1, name0), failMsg);
        }
    }

    /**
     * Validates that the fixture for {@link #assertMetaphoneEqual(String[][])} is structured correctly.
     */
    private void validateFixture(final String[][] pairs) {
        if (pairs.length == 0) {
            fail("Test fixture is empty");
        }
        for (int i = 0; i < pairs.length; i++) {
            if (pairs[i].length != 2) {
                fail("Error in test fixture: The array at index " + i + " does not contain exactly two elements.");
            }
        }
    }

    /**
     * Match data computed from http://www.lanw.com/java/phonetic/default.htm
     */
    @Test
    @DisplayName("isMetaphoneEqual should return true for a set of names that are phonetically equivalent")
    void testGroupOfSimilarNames() {
        final String[] similarNames = {
            "Cahra", "Cara", "Carey", "Cari", "Caria", "Carie", "Caro", "Carree",
            "Carri", "Carrie", "Carry", "Cary", "Cora", "Corey", "Cori", "Corie",
            "Correy", "Corri", "Corrie", "Corry", "Cory", "Gray", "Kara", "Kare",
            "Karee", "Kari", "Karia", "Karie", "Karrah", "Karrie", "Karry",
            "Kary", "Keri", "Kerri", "Kerrie", "Kerry", "Kira", "Kiri", "Kora",
            "Kore", "Kori", "Korie", "Korrie", "Korry"
        };
        // All names in the list should be phonetically equal to "Gary".
        assertMetaphoneEquals("Gary", similarNames);
    }
}