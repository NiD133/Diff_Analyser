package org.apache.commons.lang3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A test suite for the {@link LocaleUtils} class.
 */
class LocaleUtilsTest {

    /**
     * Tests that {@code toLocale(String)} correctly throws an {@link IllegalArgumentException}
     * when given a string that does not conform to the expected format.
     * <p>
     * According to the documentation, the method strictly validates the format, requiring
     * a lowercase language code. The input "uiTN-0029" is invalid because the initial
     * segment "uiTN" violates this rule due to its length and mixed case.
     */
    @Test
    @DisplayName("toLocale() should throw IllegalArgumentException for an invalidly formatted string")
    void toLocale_withInvalidFormatString_throwsIllegalArgumentException() {
        // Arrange: Define the invalid input and the expected error message.
        final String invalidLocaleString = "uiTN-0029";
        final String expectedErrorMessage = "Invalid locale format: " + invalidLocaleString;

        // Act & Assert: Verify that calling the method with invalid input throws the correct exception.
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> LocaleUtils.toLocale(invalidLocaleString)
        );

        // Assert: Verify the exception message is as expected.
        assertEquals(expectedErrorMessage, thrown.getMessage());
    }
}