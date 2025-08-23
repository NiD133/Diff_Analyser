package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link Metaphone#isMetaphoneEqual(String, String)} method.
 */
@DisplayName("Metaphone.isMetaphoneEqual")
class MetaphoneIsEqualTest extends AbstractStringEncoderTest<Metaphone> {

    @Override
    protected Metaphone createStringEncoder() {
        return new Metaphone();
    }

    @Test
    @DisplayName("should return true for a set of words that are phonetically equivalent")
    void testIsMetaphoneEqualForEquivalentSet() {
        // This test verifies that a group of words, which all produce the same Metaphone code ("RT"),
        // are considered equal by the isMetaphoneEqual method.
        // The set includes "Wright" to test the rule where an initial "WR" is coded as "R".
        final String[] equivalentWords = {"Wright", "Rota", "Rudd", "Ryde"};

        // To confirm that the set is truly equivalent, we perform a pairwise comparison.
        // Every word in the set should be metaphonically equal to every other word,
        // which also transitively confirms their equality.
        for (final String word1 : equivalentWords) {
            for (final String word2 : equivalentWords) {
                assertTrue(getStringEncoder().isMetaphoneEqual(word1, word2),
                    () -> String.format("Expected '%s' and '%s' to be metaphonically equal, but they were not.", word1, word2));
            }
        }
    }
}