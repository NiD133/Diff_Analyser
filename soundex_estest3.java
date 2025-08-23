package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Contains focused unit tests for the {@link Soundex} class.
 */
public class SoundexTest {

    /**
     * Tests that the encode method correctly handles null input by returning null,
     * which is a standard contract for encoders in this library.
     */
    @Test
    public void encodeShouldReturnNullWhenInputIsNull() {
        // Arrange: Use a standard, pre-defined Soundex instance for the test.
        // The behavior for null input should be consistent across all instances.
        Soundex soundex = Soundex.US_ENGLISH_GENEALOGY;

        // Act: Call the encode method with a null string.
        String result = soundex.encode(null);

        // Assert: The result of encoding a null string should be null.
        assertNull("Encoding a null string should return null.", result);
    }

    /**
     * Verifies that a new Soundex instance, when created with a custom mapping,
     * still defaults to the standard maximum length of 4.
     *
     * Note: The getMaxLength() method is deprecated. This test validates the
     * behavior of the current implementation.
     */
    @Test
    public void newInstanceWithCustomMapShouldHaveDefaultMaxLength() {
        // Arrange: Create a Soundex instance with an arbitrary custom mapping.
        // The content of the mapping is irrelevant for this test.
        Soundex soundex = new Soundex(new char[3]);

        // Act: Retrieve the maximum length from the new instance.
        int maxLength = soundex.getMaxLength();

        // Assert: The maximum length should be the default value of 4.
        assertEquals("The default max length should be 4.", 4, maxLength);
    }
}