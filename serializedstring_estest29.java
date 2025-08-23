package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * This test class contains tests for the {@link SerializedString} class.
 * This specific test focuses on the behavior of the equals() method.
 */
public class SerializedString_ESTestTest29 extends SerializedString_ESTest_scaffolding {

    /**
     * Tests that the equals() method is reflexive.
     * An object instance must always be equal to itself.
     */
    @Test
    public void testEquals_withSameInstance_returnsTrue() {
        // Arrange: Create an instance of SerializedString.
        SerializedString serializedString = new SerializedString("any string");

        // Act & Assert: Verify that the instance is equal to itself.
        // This confirms the reflexive property of the equals() method as per the Object contract.
        assertTrue("An instance of SerializedString should be equal to itself.",
                   serializedString.equals(serializedString));
    }
}