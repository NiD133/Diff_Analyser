package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link Soundex} class.
 */
public class SoundexTest {

    /**
     * Tests that a Soundex instance created with the default constructor
     * has a maximum length of 4, as per the standard definition.
     *
     * Note: The {@code getMaxLength()} method is deprecated, but this test
     * ensures its behavior remains consistent for backward compatibility.
     */
    @Test
    public void defaultMaxLengthShouldBeFour() {
        // Arrange
        final Soundex soundex = new Soundex();
        final int expectedLength = 4;

        // Act
        final int actualLength = soundex.getMaxLength();

        // Assert
        assertEquals("The default max length should be 4", expectedLength, actualLength);
    }
}