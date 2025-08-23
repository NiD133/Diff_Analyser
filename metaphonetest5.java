package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests the {@code isMetaphoneEqual} method of the {@link Metaphone} class.
 */
@DisplayName("Metaphone isMetaphoneEqual Test")
public class MetaphoneTest extends AbstractStringEncoderTest<Metaphone> {

    @Override
    protected Metaphone createStringEncoder() {
        return new Metaphone();
    }

    @ParameterizedTest
    @CsvSource({
        "Case,      case",      // Same word, different case
        "CASE,      Case",      // Same word, different case
        "caSe,      cAsE",      // Same word, different case
        "quick,     cookie",    // Phonetically similar
        "aero,      arrow",     // Phonetically similar
        "AT,        AD",        // Phonetically similar (from original Metaphone paper)
        "PHILIP,    FILIP"      // Phonetically similar (from original Metaphone paper)
    })
    @DisplayName("Should return true for strings that are phonetically alike")
    void shouldConfirmMetaphoneEqualityForSimilarStrings(final String string1, final String string2) {
        // The isMetaphoneEqual method should be symmetric.
        // We test both (a, b) and (b, a) to ensure this.
        assertTrue(getStringEncoder().isMetaphoneEqual(string1, string2),
            () -> "Expected '" + string1 + "' and '" + string2 + "' to be metaphonically equal.");

        assertTrue(getStringEncoder().isMetaphoneEqual(string2, string1),
            () -> "Symmetry check failed: Expected '" + string2 + "' and '" + string1 + "' to be metaphonically equal.");
    }
}