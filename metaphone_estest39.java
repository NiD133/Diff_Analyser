package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test cases for the Metaphone class, focusing on specific encoding rules.
 */
public class MetaphoneTest {

    /**
     * Tests that a word starting with "WR" is encoded as "R".
     *
     * <p>The Metaphone algorithm specifies that if a word begins with "WR", the 'W' is dropped.
     * This test uses the input "wr`hH" to also verify that the implementation correctly handles:
     * <ul>
     *     <li>Case-insensitivity (by converting the input to uppercase).</li>
     *     <li>Non-alphabetic characters (by ignoring them).</li>
     * </ul>
     * The test also confirms that the Metaphone object is initialized with the default maximum code length.
     * </p>
     */
    @Test
    public void shouldEncodeWordStartingWithWRAsR() {
        // Arrange
        final Metaphone metaphone = new Metaphone();
        final String input = "wr`hH"; // This normalizes to "WRHH" before encoding
        final String expectedCode = "R";

        // Act
        final String actualCode = metaphone.metaphone(input);

        // Assert
        assertEquals("Encoding for a word starting with 'WR' should be 'R'", expectedCode, actualCode);
        assertEquals("The default max code length should be 4", 4, metaphone.getMaxCodeLen());
    }
}