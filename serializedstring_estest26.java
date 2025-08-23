package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Contains tests for the constructor of the {@link SerializedString} class.
 */
public class SerializedStringConstructorTest {

    /**
     * Verifies that the SerializedString constructor throws a NullPointerException
     * when a null string is provided as input. This ensures that the class
     * correctly handles invalid arguments according to its contract.
     */
    @Test
    public void constructingWithNullStringShouldThrowNullPointerException() {
        // Arrange: Define the expected exception message from the class contract.
        String expectedMessage = "Null String illegal for SerializedString";

        // Act & Assert: Execute the constructor with null input and verify that the
        // correct exception is thrown.
        NullPointerException thrownException = assertThrows(
            NullPointerException.class,
            () -> new SerializedString(null)
        );

        // Assert: Further verify that the exception message is as expected.
        assertEquals(expectedMessage, thrownException.getMessage());
    }
}