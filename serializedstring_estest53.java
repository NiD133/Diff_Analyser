package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link SerializedString} class, focusing on its serialization behavior.
 */
public class SerializedStringTest {

    /**
     * Verifies that directly calling readResolve() on a normally instantiated
     * SerializedString throws a NullPointerException.
     * <p>
     * The {@code readResolve()} method is part of Java's deserialization contract and is
     * intended to be called by the JVM only after {@code readObject()} has populated the
     * necessary transient fields. Invoking it directly bypasses this setup, leading to an
     * internal state where the string value is null, which the constructor correctly rejects.
     */
    @Test
    public void readResolve_whenInvokedDirectly_shouldThrowNullPointerException() {
        // Arrange: Create a SerializedString instance through its normal constructor.
        // The actual string content is irrelevant for this test.
        SerializedString serializedString = new SerializedString("any-string-value");

        // Act & Assert
        try {
            // Attempt to call a method that should only be invoked during deserialization.
            serializedString.readResolve();
            fail("A NullPointerException was expected but not thrown.");
        } catch (NullPointerException e) {
            // Verify that the exception is the one we expect from the constructor's null check.
            String expectedMessage = "Null String illegal for SerializedString";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}