package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link org.apache.commons.codec.language.RefinedSoundex} class.
 */
public class RefinedSoundexTest {

    @Test
    public void encodeShouldReturnNullWhenInputIsNull() {
        // Arrange
        RefinedSoundex soundexEncoder = new RefinedSoundex();

        // Act
        String result = soundexEncoder.encode(null);

        // Assert
        assertNull("Encoding a null string should return null.", result);
    }
}