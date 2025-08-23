package org.jsoup.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link Validate}.
 */
public class ValidateTest {

    @Test
    @DisplayName("wtf() should always throw an IllegalStateException")
    void wtfAlwaysThrowsIllegalStateException() {
        // Arrange
        String expectedMessage = "Unexpected state reached";

        // Act & Assert
        // Verify that calling Validate.wtf() throws the correct exception.
        IllegalStateException thrown = assertThrows(
            IllegalStateException.class,
            () -> Validate.wtf(expectedMessage)
        );

        // Verify that the exception has the expected message.
        assertEquals(expectedMessage, thrown.getMessage());
    }
}