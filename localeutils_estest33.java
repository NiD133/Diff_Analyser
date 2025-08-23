package org.apache.commons.lang3;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for {@link LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Tests that toLocale() throws an IllegalArgumentException when given a string
     * with an invalid format.
     */
    @Test
    void toLocaleShouldThrowIllegalArgumentExceptionForInvalidFormat() {
        // Arrange: Define an input string that does not conform to the expected locale format.
        final String invalidLocaleString = "q_]:a$7mI";
        final String expectedMessage = "Invalid locale format: " + invalidLocaleString;

        // Act: Call the method under test and expect an exception.
        // The assertThrows method captures the thrown exception for further inspection.
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> LocaleUtils.toLocale(invalidLocaleString)
        );

        // Assert: Verify that the captured exception has the expected message.
        assertEquals(expectedMessage, thrown.getMessage());
    }
}