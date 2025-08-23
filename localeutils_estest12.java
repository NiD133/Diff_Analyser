package org.apache.commons.lang3;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link org.apache.commons.lang3.LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Tests that toLocale() throws an IllegalArgumentException when given a string
     * with a format that cannot be parsed into a valid Locale.
     */
    @Test
    public void toLocaleShouldThrowExceptionForInvalidFormatString() {
        // Arrange: Define an input string that does not conform to the expected locale format.
        final String invalidLocaleString = "_9DSQCbSw^!e Mr4eg6";
        final String expectedMessage = "Invalid locale format: " + invalidLocaleString;

        // Act & Assert: Verify that calling toLocale with the invalid string throws the correct exception
        // with a descriptive message.
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> LocaleUtils.toLocale(invalidLocaleString)
        );

        assertEquals(expectedMessage, thrown.getMessage());
    }
}