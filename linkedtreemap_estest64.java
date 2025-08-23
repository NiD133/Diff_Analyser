package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link LinkedTreeMap}.
 */
public class LinkedTreeMapTest {

    /**
     * Verifies that attempting to insert an entry with a null key
     * throws a NullPointerException, as null keys are not permitted.
     */
    @Test
    public void put_withNullKey_shouldThrowNullPointerException() {
        // Arrange
        LinkedTreeMap<Integer, String> map = new LinkedTreeMap<>();
        String anyValue = "test_value";

        // Act & Assert
        try {
            map.put(null, anyValue);
            fail("Expected a NullPointerException because the key is null.");
        } catch (NullPointerException e) {
            // Verify that the exception is the one we expect
            assertEquals("key == null", e.getMessage());
        }
    }
}