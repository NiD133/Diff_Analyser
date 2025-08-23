package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for the {@link RefinedSoundex} class.
 */
public class RefinedSoundexTest {

    /**
     * Tests that the soundex method handles null input gracefully by returning null.
     */
    @Test
    public void soundexShouldReturnNullForNullInput() {
        // Arrange
        RefinedSoundex refinedSoundex = new RefinedSoundex();

        // Act
        String result = refinedSoundex.soundex(null);

        // Assert
        assertNull("The soundex of a null string should be null", result);
    }
}