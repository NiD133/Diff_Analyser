package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Contains unit tests for the {@link SerializedString#equals(Object)} method.
 */
public class SerializedStringTest {

    /**
     * Verifies that the {@code equals} method returns {@code false} when a
     * {@code SerializedString} instance is compared to {@code null}.
     * This is a standard requirement of the {@code Object.equals()} contract.
     */
    @Test
    public void equalsShouldReturnFalseWhenComparedToNull() {
        // Arrange: Create an instance of SerializedString.
        // The actual string content is irrelevant for this test.
        SerializedString serializedString = new SerializedString("");

        // Act & Assert: The result of comparing with null should be false.
        assertFalse("A SerializedString instance should never be equal to null.",
                serializedString.equals(null));
    }
}