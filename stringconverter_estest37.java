package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for {@link StringConverter}.
 */
public class StringConverterTest {

    /**
     * Tests that getDurationMillis throws an IllegalArgumentException
     * when given an empty string, as it's not a valid ISO-8601 duration format.
     */
    @Test
    public void getDurationMillis_shouldThrowIllegalArgumentException_forEmptyString() {
        // Arrange
        StringConverter converter = StringConverter.INSTANCE;
        String emptyString = "";
        String expectedMessage = "Invalid format: \"\"";

        // Act
        IllegalArgumentException thrownException = assertThrows(
            IllegalArgumentException.class,
            () -> converter.getDurationMillis(emptyString)
        );

        // Assert
        assertEquals(expectedMessage, thrownException.getMessage());
    }
}