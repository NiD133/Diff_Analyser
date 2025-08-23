package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the Metaphone class.
 */
public class MetaphoneTest {

    /**
     * Tests a specific rule of the Metaphone algorithm where "TH" is encoded as "0" (theta sound).
     */
    @Test
    public void shouldEncodeThAsZero() {
        // Arrange
        Metaphone metaphone = new Metaphone();
        String input = "TH";
        String expectedCode = "0";

        // Act
        String actualCode = metaphone.metaphone(input);

        // Assert
        assertEquals("The Metaphone code for 'TH' should be '0'", expectedCode, actualCode);
    }
}