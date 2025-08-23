package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link org.apache.commons.lang3.CharSetUtils}.
 */
public class CharSetUtilsTest {

    /**
     * Tests that squeezing an empty string results in an empty string,
     * regardless of the character set provided.
     */
    @Test
    public void testSqueezeWithEmptyStringReturnsEmptyString() {
        // Arrange
        final String input = "";
        
        // The Javadoc for squeeze() specifies: CharSetUtils.squeeze("", *) = ""
        // This test verifies that behavior, using an array of nulls as the set
        // to confirm it holds even for unconventional inputs.
        final String[] setWithNulls = new String[4];

        // Act
        final String result = CharSetUtils.squeeze(input, setWithNulls);

        // Assert
        assertEquals("Squeezing an empty string should always return an empty string.", "", result);
    }
}