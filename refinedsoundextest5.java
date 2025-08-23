package org.apache.commons.codec.language;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the {@link RefinedSoundex} class.
 */
public class RefinedSoundexTest extends AbstractStringEncoderTest<RefinedSoundex> {

    @Override
    protected RefinedSoundex createStringEncoder() {
        return new RefinedSoundex();
    }

    @Test
    @DisplayName("The soundex() method should correctly encode a simple word")
    void shouldCorrectlyEncodeWordWithSoundexMethod() {
        // Arrange
        final String input = "dogs";
        final String expectedEncoding = "D6043";

        // Act
        final String actualEncoding = this.stringEncoder.soundex(input);

        // Assert
        assertEquals(expectedEncoding, actualEncoding);
    }
}