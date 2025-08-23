package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests for the {@link JsonArray} class.
 */
public class JsonArrayTest {

    /**
     * Verifies that the constructor {@link JsonArray#JsonArray(int)} throws
     * an IllegalArgumentException when provided with a negative capacity.
     */
    @Test
    public void constructor_shouldThrowIllegalArgumentException_whenCapacityIsNegative() {
        int negativeCapacity = -1;
        try {
            new JsonArray(negativeCapacity);
            fail("Expected an IllegalArgumentException to be thrown for negative capacity.");
        } catch (IllegalArgumentException expected) {
            // The constructor delegates to ArrayList, which throws this exception.
            // We verify the message to ensure the failure is for the expected reason.
            assertEquals("Illegal Capacity: " + negativeCapacity, expected.getMessage());
        }
    }
}