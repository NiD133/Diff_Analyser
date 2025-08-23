package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link Metaphone} encoder, focusing on specific edge cases and rules.
 *
 * <p>This class was improved from a previous version named {@code MetaphoneTestTest2}.
 * The improvements include splitting a multi-concern test into two focused tests,
 * removing unused helper methods, and adding descriptive names and comments.</p>
 */
public class MetaphoneTest extends AbstractStringEncoderTest<Metaphone> {

    @Override
    protected Metaphone createStringEncoder() {
        return new Metaphone();
    }

    @Test
    @DisplayName("A word starting with 'GN' should be encoded beginning with 'N'")
    void testWordStartingWithGnIsEncodedAsN() {
        // This test verifies the Metaphone rule where a word starting with "GN" (e.g., "Gnu")
        // is encoded with an initial 'N', as the 'G' is considered silent.
        assertEquals("N", getStringEncoder().metaphone("GNU"));
    }

    @Test
    @DisplayName("A word ending in 'GNED' should correctly encode the silent 'G'")
    void testSilentGInGnedAtWordEnd() {
        // This test verifies the rule for handling "GNED" at the end of a word.
        // In "SIGNED", the "GN" is effectively silent, and the "D" is encoded as "T".
        // The expected Metaphone code is "SNT".
        // The original test had a misleading comment suggesting this rule was not working,
        // but the implementation correctly produces the expected result.
        assertEquals("SNT", getStringEncoder().metaphone("SIGNED"));
    }
}