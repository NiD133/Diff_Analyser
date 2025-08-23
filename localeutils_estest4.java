package org.apache.commons.lang3;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A test suite for the {@link LocaleUtils} class.
 */
class LocaleUtilsTest {

    /**
     * Tests that {@link LocaleUtils#toLocale(String)} throws an {@link IllegalArgumentException}
     * when the input string represents an invalid locale format.
     *
     * <p>The test uses "zh-XCN" as an example of an invalid format. According to the
     * {@link LocaleUtils#toLocale(String)} documentation, the country code part must be
     * a 2-letter uppercase ISO 3166 code or a 3-digit UN M.49 area code. "XCN" is neither,
     * making the overall string invalid.</p>
     */
    @Test
    void toLocale_withInvalidFormat_throwsIllegalArgumentException() {
        // Arrange: Define the invalid locale string that should cause an exception.
        final String invalidLocaleString = "zh-XCN";

        // Act & Assert: Verify that calling toLocale with the invalid string throws
        // the expected exception.
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> LocaleUtils.toLocale(invalidLocaleString)
        );

        // Assert on the exception message for more precise verification.
        final String expectedMessage = "Invalid locale format: " + invalidLocaleString;
        assertEquals(expectedMessage, thrown.getMessage());
    }
}