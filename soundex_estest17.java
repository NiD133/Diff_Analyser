package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link Soundex} class.
 */
public class SoundexTest {

    /**
     * Tests that the soundex method correctly handles a null input by returning null,
     * as specified by its behavior.
     */
    @Test
    public void soundexShouldReturnNullForNullInput() {
        // Arrange
        Soundex soundex = new Soundex();
        String nullInput = null;

        // Act
        String result = soundex.soundex(nullInput);

        // Assert
        assertNull("The soundex of a null string should be null.", result);
    }
}