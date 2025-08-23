package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link RefinedSoundex} class, focusing on its custom mapping capabilities.
 */
public class RefinedSoundexTest {

    /**
     * Tests that the getMappingCode() method correctly returns a character
     * from a custom mapping provided during instantiation.
     */
    @Test
    public void getMappingCodeShouldReturnCorrectCharFromCustomMapping() {
        // Arrange: Create a custom mapping where the code for 'X' (the 24th letter) is 'c'.
        // The rest of the mapping is standard for clarity.
        final String customMapping = "ABCDEFGHIJKLMNOPQRSTUVWcYZ";
        final RefinedSoundex soundex = new RefinedSoundex(customMapping);

        // Act: Get the mapping code for the character 'X'.
        final char mappingCode = soundex.getMappingCode('X');

        // Assert: Verify that the returned code is 'c', as defined in our custom mapping.
        assertEquals('c', mappingCode);
    }
}