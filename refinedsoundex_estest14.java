package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link RefinedSoundex} class, focusing on custom mapping features.
 */
public class RefinedSoundexTest {

    /**
     * Tests that the encode method correctly uses a custom mapping provided as a String
     * during instantiation. The custom mapping in this test assigns simple numeric codes
     * to the first few letters of the alphabet.
     */
    @Test
    public void encodeShouldUseCustomMappingProvidedAsString() {
        // Arrange
        // A custom mapping where A=1, B=2, C=3, and all other letters map to 0.
        final String customMapping = "12300000000000000000000000";
        final RefinedSoundex soundex = new RefinedSoundex(customMapping);
        final String input = "cba";

        // The expected encoding is derived as follows:
        // 1. The first letter 'C' is kept.
        // 2. The code for 'B' is '2' from our custom mapping.
        // 3. The code for 'A' is '1' from our custom mapping.
        // Result: "C21"
        final String expectedEncoding = "C21";

        // Act
        final String actualEncoding = soundex.encode(input);

        // Assert
        assertEquals("The custom mapping should be correctly applied during encoding",
                expectedEncoding, actualEncoding);
    }
}