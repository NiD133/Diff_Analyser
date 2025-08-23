package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the Metaphone class, focusing on specific encoding rules.
 */
public class MetaphoneTest {

    /**
     * Tests that the Metaphone algorithm correctly encodes the string "TCH" as "X".
     * This is a specific rule within the Metaphone specification.
     */
    @Test
    public void shouldEncodeTchAsX() {
        // Arrange
        Metaphone metaphone = new Metaphone();
        String input = "TCH";
        String expectedEncoding = "X";

        // Act
        String actualEncoding = metaphone.metaphone(input);

        // Assert
        assertEquals("The encoding for 'TCH' should be 'X'", expectedEncoding, actualEncoding);
    }
}