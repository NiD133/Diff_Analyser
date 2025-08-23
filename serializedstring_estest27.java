package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Unit tests for the {@link SerializedString} class, focusing on its equality logic.
 */
public class SerializedStringTest {

    /**
     * Verifies that two distinct {@link SerializedString} instances created with the
     * same underlying string value are considered equal by the {@code equals} method.
     */
    @Test
    public void equals_shouldReturnTrue_whenInstancesHaveSameValue() {
        // Arrange
        String sharedValue = "";
        SerializedString instance1 = new SerializedString(sharedValue);
        SerializedString instance2 = new SerializedString(sharedValue);

        // Assert that we are comparing two different objects
        assertNotSame("Precondition failed: instances should be different objects.", instance1, instance2);

        // Act & Assert
        // The assertEquals method uses the .equals() implementation for comparison.
        // This assertion verifies that two distinct instances with identical
        // content are correctly evaluated as equal.
        assertEquals(instance1, instance2);
    }
}