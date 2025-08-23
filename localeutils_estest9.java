package org.apache.commons.lang3;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link LocaleUtils}.
 */
class LocaleUtilsTest {

    /**
     * Tests that toLocale() throws an IllegalArgumentException for an invalid format
     * that starts with a separator.
     */
    @Test
    void toLocale_shouldThrowIllegalArgumentException_whenStringStartsWithSeparator() {
        // Arrange: Define an invalid locale string that starts with a separator.
        // The LocaleUtils.toLocale method expects a format like "en_GB", not "_GB".
        final String invalidLocaleString = "-CIIN";

        // Act & Assert: Verify that calling toLocale with the invalid string
        // throws the expected exception with the correct message.
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> LocaleUtils.toLocale(invalidLocaleString)
        );

        // Further assert that the exception message is informative and correct.
        assertEquals("Invalid locale format: " + invalidLocaleString, thrown.getMessage());
    }
}