package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests the {@link Metaphone#isMetaphoneEqual(String, String)} method.
 */
class MetaphoneTest extends AbstractStringEncoderTest<Metaphone> {

    @Override
    protected Metaphone createStringEncoder() {
        return new Metaphone();
    }

    /**
     * The Metaphone algorithm should identify words with similar vowel sounds and structures
     * as phonetically equal. This test verifies that "Ray" is considered equal to
     * variations like "Rey", "Roi", etc.
     *
     * Match data was originally computed from an external reference:
     * http://www.lanw.com/java/phonetic/default.htm
     */
    @DisplayName("isMetaphoneEqual should return true for phonetically similar words")
    @ParameterizedTest(name = "Comparing \"Ray\" with \"{0}\"")
    @ValueSource(strings = {"Ray", "Rey", "Roi", "Roy", "Ruy"})
    void isMetaphoneEqual_shouldReturnTrue_forPhoneticallySimilarWords(final String similarWord) {
        // Arrange
        final String sourceWord = "Ray";

        // Act & Assert
        // The isMetaphoneEqual method should be symmetric, but we test against a fixed source
        // for clarity. All words in the ValueSource produce the same Metaphone code ("R").
        assertTrue(getStringEncoder().isMetaphoneEqual(sourceWord, similarWord),
            () -> "Expected '" + sourceWord + "' to be phonetically equal to '" + similarWord + "'");
    }
}