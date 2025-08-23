package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Metaphone} class.
 */
public class MetaphoneTest {

    /**
     * Tests that the Metaphone algorithm correctly encodes the "CH" combination as "X".
     * This test also implicitly verifies that non-alphabetic characters are handled
     * correctly by the algorithm.
     */
    @Test
    public void metaphoneShouldEncodeChAsX() {
        // Arrange: Create a Metaphone instance and define the input string.
        Metaphone metaphone = new Metaphone();
        String input = "CH;";

        // Act: Generate the Metaphone code for the input string.
        String metaphoneCode = metaphone.metaphone(input);

        // Assert: Verify that the generated code is "X" as expected.
        assertEquals("The metaphone code for 'CH;' should be 'X'", "X", metaphoneCode);
    }

    /**
     * Tests that a new Metaphone instance is initialized with the correct default
     * maximum code length, which is 4.
     */
    @Test
    public void shouldInitializeWithDefaultMaxCodeLengthOf4() {
        // Arrange: Create a new Metaphone instance.
        Metaphone metaphone = new Metaphone();

        // Act: Retrieve the maximum code length.
        int maxCodeLen = metaphone.getMaxCodeLen();

        // Assert: Verify that the length is the expected default value.
        assertEquals("Default max code length should be 4", 4, maxCodeLen);
    }
}