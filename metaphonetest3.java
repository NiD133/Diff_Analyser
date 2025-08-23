package org.apache.commons.codec.language;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the {@link Metaphone} algorithm.
 */
public class MetaphoneTest extends AbstractStringEncoderTest<Metaphone> {

    @Override
    protected Metaphone createStringEncoder() {
        return new Metaphone();
    }

    @Test
    @DisplayName("Should discard a silent 'H' that follows a 'G'")
    void shouldDiscardSilentHAfterG() {
        // This test verifies a specific rule of the Metaphone algorithm.
        // For example, in "GHENT", the 'H' is silent and 'G' is treated as 'K'.
        // In "BAUGH", the 'GH' combination at the end is silent.

        assertEquals("KNT", getStringEncoder().metaphone("GHENT"), "Metaphone of 'GHENT' should be 'KNT'");
        assertEquals("B", getStringEncoder().metaphone("BAUGH"), "Metaphone of 'BAUGH' should be 'B'");
    }
}