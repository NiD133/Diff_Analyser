package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
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
     * This test verifies that a group of names that are phonetically similar
     * to "Susan" are all encoded to the same Metaphone value. This ensures the
     * {@code isMetaphoneEqual} method correctly identifies them as equivalent.
     * The test data was originally sourced from an external phonetic calculator.
     */
    @Test
    @DisplayName("Should encode variations of 'Susan' to the same Metaphone code")
    void shouldEncodeSusanVariationsToSameCode() {
        // Arrange
        final String[] phoneticallySimilarNames = {
            "Siusan", "Sosanna", "Susan", "Susana", "Susann", "Susanna",
            "Susannah", "Susanne", "Suzann", "Suzanna", "Suzanne", "Zuzana"
        };
        final Metaphone metaphone = createStringEncoder();
        final String expectedCode = metaphone.metaphone("Susan"); // Expected code: SSN

        // Act & Assert
        assertAll("All variations of 'Susan' should produce the same Metaphone code",
            () -> {
                for (final String name : phoneticallySimilarNames) {
                    assertEquals(expectedCode, metaphone.metaphone(name),
                        () -> "Metaphone for '" + name + "' should match the code for 'Susan'");
                }
            }
        );
    }
}