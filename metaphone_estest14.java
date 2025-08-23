package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Metaphone} class.
 */
public class MetaphoneTest {

    /**
     * Tests that the Metaphone algorithm correctly encodes the string "CH" as "X".
     * This test case verifies a specific encoding rule documented in the Metaphone algorithm.
     */
    @Test
    public void metaphoneShouldEncodeChAsX() {
        // Arrange: Create a Metaphone encoder instance and define the input string.
        Metaphone metaphone = new Metaphone();
        String input = "CH";

        // Act: Encode the input string.
        String result = metaphone.metaphone(input);

        // Assert: Verify that the encoded result is "X".
        assertEquals("X", result);
    }
}