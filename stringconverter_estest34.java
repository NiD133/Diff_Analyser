package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for {@link StringConverter}.
 */
public class StringConverterTest {

    /**
     * Tests that getDurationMillis() throws an IllegalArgumentException when provided with a string
     * that does not conform to the ISO 8601 duration format.
     * The format "PT2f" is invalid because 'f' is not a recognized duration field designator.
     */
    @Test
    public void getDurationMillis_withInvalidFormatString_shouldThrowIllegalArgumentException() {
        // Arrange
        final StringConverter converter = new StringConverter();
        final String invalidDurationString = "PT2f";
        final String expectedErrorMessage = "Invalid format: \"" + invalidDurationString + "\"";

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> converter.getDurationMillis(invalidDurationString)
        );

        assertEquals(expectedErrorMessage, thrown.getMessage());
    }
}