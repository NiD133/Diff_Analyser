package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Metaphone} class.
 */
public class MetaphoneTest {

    /**
     * Tests that the Metaphone algorithm correctly encodes the string "CH" to "X".
     * This covers a specific rule in the Metaphone encoding process.
     */
    @Test
    public void testEncodeCHasX() {
        // Arrange: Create a Metaphone encoder instance and define the input.
        Metaphone metaphone = new Metaphone();
        final String input = "CH";

        // Act: Encode the input string.
        final String metaphoneCode = metaphone.encode(input);

        // Assert: Verify that the encoded result is "X".
        assertEquals("X", metaphoneCode);
    }
}