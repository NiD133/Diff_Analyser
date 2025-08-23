package com.fasterxml.jackson.core.io;

import org.junit.Test;
import java.nio.ByteBuffer;
import static org.junit.Assert.assertFalse;

/**
 * Contains unit tests for the {@link SerializedString#equals(Object)} method.
 */
public class SerializedStringEqualsTest {

    /**
     * Tests that the equals() method returns false when a SerializedString
     * is compared with an object of a completely different type. This is a
     * standard contract for the equals() method.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparedWithDifferentType() {
        // Arrange: Create a SerializedString instance and an object of another type.
        SerializedString serializedString = new SerializedString("some value");
        Object differentTypeObject = ByteBuffer.allocate(0);

        // Act: Call the equals method with the different type object.
        boolean result = serializedString.equals(differentTypeObject);

        // Assert: The result should be false.
        assertFalse("SerializedString should not be equal to an object of a different type.", result);
    }
}