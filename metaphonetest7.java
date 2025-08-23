package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the isMetaphoneEqual method of the Metaphone class.
 */
class MetaphoneTest {

    private Metaphone metaphone;

    @BeforeEach
    void setUp() {
        this.metaphone = new Metaphone();
    }

    /**
     * Tests the initial AE case from the original algorithm documentation.
     * Match data was originally computed from http://www.lanw.com/java/phonetic/default.htm
     */
    @Test
    @DisplayName("isMetaphoneEqual should return true for 'Aero' and 'Eure'")
    void isMetaphoneEqual_shouldReturnTrue_forSimilarSoundingWords() {
        // Given
        final String word1 = "Aero";
        final String word2 = "Eure";

        // When & Then
        // The isMetaphoneEqual method should be symmetric.
        assertTrue(metaphone.isMetaphoneEqual(word1, word2),
            () -> "Expected '" + word1 + "' and '" + word2 + "' to be metaphonically equal.");

        assertTrue(metaphone.isMetaphoneEqual(word2, word1),
            () -> "Expected '" + word2 + "' and '" + word1 + "' to be metaphonically equal (testing for symmetry).");
    }
}