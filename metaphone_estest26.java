package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link Metaphone} class.
 */
public class MetaphoneTest {

    /**
     * Tests that the metaphone() method correctly encodes a string that includes:
     * 1. A non-alphabetic prefix character ('!').
     * 2. Mixed case letters ('g', 'I', 'j').
     * 3. A "soft G" phonetic rule (where 'G' followed by 'I' is encoded as 'J').
     *
     * The original, auto-generated test also asserted the default maximum code length.
     * That assertion has been removed from this test to follow the best practice of
     * testing only a single concept per test case. A separate test for the default
     * configuration would be more appropriate.
     */
    @Test
    public void metaphoneShouldCorrectlyEncodeStringWithSoftGAndNonAlphaPrefix() {
        // Arrange
        final Metaphone metaphone = new Metaphone();
        final String input = "!gIj";
        final String expectedCode = "JJ";

        // Act
        final String actualCode = metaphone.metaphone(input);

        // Assert
        assertEquals("Encoding should ignore the non-alphabetic prefix, be case-insensitive, and apply the 'GI' -> 'J' rule.",
                     expectedCode, actualCode);
    }
}