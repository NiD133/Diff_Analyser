package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Metaphone} class.
 */
public class MetaphoneTest {

    /**
     * Tests that the encode method handles null input gracefully by returning an empty string.
     * This is a common convention for string encoders.
     */
    @Test
    public void encodeShouldReturnEmptyStringForNullInput() {
        // Arrange
        Metaphone metaphone = new Metaphone();
        String expectedEncoding = "";

        // Act
        String actualEncoding = metaphone.encode(null);

        // Assert
        assertEquals("Encoding a null string should return an empty string.", expectedEncoding, actualEncoding);
    }
}