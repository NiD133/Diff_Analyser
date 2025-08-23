package org.joda.time.convert;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for {@link StringConverter}.
 */
public class StringConverterTest {

    /**
     * Tests that getDurationMillis() throws an IllegalArgumentException
     * when provided with a string that is not a valid ISO 8601 duration format.
     */
    @Test
    public void getDurationMillis_shouldThrowException_forInvalidDurationFormat() {
        // Arrange
        final StringConverter converter = StringConverter.INSTANCE;
        final String invalidDurationString = "Pt2.is";
        final String expectedErrorMessage = "Invalid format: \"" + invalidDurationString + "\"";

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> converter.getDurationMillis(invalidDurationString)
        );

        assertEquals(expectedErrorMessage, thrown.getMessage());
    }
}