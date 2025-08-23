package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the Metaphone class.
 */
public class MetaphoneTest {

    /**
     * Tests that a simple string of consonants is encoded correctly according to the Metaphone algorithm.
     * The original Metaphone algorithm encodes 'G' as 'K' in this context.
     */
    @Test
    public void shouldEncodeSimpleConsonantString() {
        // Arrange
        final Metaphone metaphone = new Metaphone();
        final String input = "SFG";
        final String expectedEncoding = "SFK";

        // Act
        final String actualEncoding = metaphone.metaphone(input);

        // Assert
        assertEquals("The Metaphone encoding for 'SFG' should be 'SFK'",
                     expectedEncoding,
                     actualEncoding);
    }
}