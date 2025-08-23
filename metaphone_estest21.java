package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Metaphone} class.
 */
public class MetaphoneTest {

    /**
     * Tests that the metaphone() method correctly handles null input
     * by returning an empty string, which is the expected behavior for
     * invalid or empty inputs.
     */
    @Test
    public void metaphoneShouldReturnEmptyStringForNullInput() {
        // Arrange: Create an instance of the class under test.
        Metaphone metaphone = new Metaphone();

        // Act: Call the method with null input.
        String result = metaphone.metaphone(null);

        // Assert: Verify that the result is an empty string.
        assertEquals("Encoding a null string should result in an empty string.", "", result);
    }
}