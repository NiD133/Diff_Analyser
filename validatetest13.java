package org.jsoup.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link Validate#assertFail(String)}.
 */
public class ValidateTestTest13 {

    @Test
    @DisplayName("assertFail should always throw an IllegalStateException")
    void assertFail_alwaysThrowsException() {
        // Arrange: Define the expected exception message
        final String expectedMessage = "This should fail";

        // Act & Assert: Verify that calling assertFail throws the correct exception with the correct message.
        // The `assertThrows` method is the standard way to test for exceptions in JUnit 5.
        IllegalStateException thrownException = assertThrows(
            IllegalStateException.class,
            () -> Validate.assertFail(expectedMessage),
            "Validate.assertFail() was expected to throw an IllegalStateException, but it did not."
        );

        // Assert: Further verify that the exception's message is the one we provided.
        assertEquals(expectedMessage, thrownException.getMessage(), "The exception message did not match the expected value.");
    }
}