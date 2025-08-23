package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

/**
 * Unit tests for the {@link RefinedSoundex} class.
 */
public class RefinedSoundexTest {

    @Test
    public void differenceOfTwoNullStringsShouldReturnZero() throws EncoderException {
        // Arrange
        RefinedSoundex refinedSoundex = new RefinedSoundex();
        
        // Act
        int difference = refinedSoundex.difference(null, null);
        
        // Assert
        assertEquals("The difference between two null strings should be 0", 0, difference);
    }
}