package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Metaphone} class.
 */
public class MetaphoneTest {

    /**
     * Tests that the Metaphone algorithm correctly handles words starting with "GN",
     * where the 'G' is silent. For example, "gnome" is encoded as "NM".
     */
    @Test
    public void shouldEncodeWordStartingWithGNAsN() {
        // Arrange
        final Metaphone metaphone = new Metaphone();
        final String input = "GN";
        final String expectedCode = "N";

        // Act
        final String actualCode = metaphone.metaphone(input);

        // Assert
        assertEquals("The metaphone code for 'GN' should be 'N'", expectedCode, actualCode);
    }
}