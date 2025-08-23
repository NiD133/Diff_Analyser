package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Soundex} class.
 */
public class SoundexTest {

    /**
     * Tests that the default maximum length of a Soundex code is 4, as per the standard algorithm definition.
     * This behavior should hold true regardless of the mapping used for the constructor.
     * Note: The getMaxLength() method is deprecated.
     */
    @Test
    public void getMaxLengthShouldReturnDefaultValue() {
        // Arrange
        // The standard Soundex algorithm produces a 4-character code.
        final int expectedMaxLength = 4;
        
        // The constructor accepts a custom mapping, but this should not affect the default max length.
        // We use the standard US English mapping for a realistic and clear example.
        final Soundex soundex = new Soundex(Soundex.US_ENGLISH_MAPPING_STRING);

        // Act
        final int actualMaxLength = soundex.getMaxLength();

        // Assert
        assertEquals("The default max length of a Soundex code should be 4.", expectedMaxLength, actualMaxLength);
    }
}