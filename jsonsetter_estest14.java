package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test suite focuses on the {@link JsonSetter.Value} class, specifically its
 * factory methods for creating and modifying instances.
 */
public class JsonSetterValueTest {

    /**
     * Verifies that the `withValueNulls(Nulls, Nulls)` method correctly creates a new
     * {@link JsonSetter.Value} instance with updated settings for both value and content nulls.
     * It also ensures the original instance remains unchanged, confirming immutability.
     */
    @Test
    public void withValueNulls_shouldUpdateBothValueAndContentNulls() {
        // Arrange: Start with a default JsonSetter.Value instance.
        JsonSetter.Value initialValue = JsonSetter.Value.empty();
        final Nulls newNullsSetting = Nulls.SKIP;

        // Act: Create a new instance by updating both value and content nulls settings.
        JsonSetter.Value updatedValue = initialValue.withValueNulls(newNullsSetting, newNullsSetting);

        // Assert: Verify that the new instance has the updated settings.
        assertNotSame("A new instance should be returned to ensure immutability.", initialValue, updatedValue);

        assertEquals("Value nulls should be updated to SKIP.",
                newNullsSetting, updatedValue.getValueNulls());
        assertEquals("Content nulls should be updated to SKIP.",
                newNullsSetting, updatedValue.getContentNulls());

        // Further assert that the original instance was not modified.
        assertEquals("Original value nulls should remain DEFAULT.",
                Nulls.DEFAULT, initialValue.getValueNulls());
        assertEquals("Original content nulls should remain DEFAULT.",
                Nulls.DEFAULT, initialValue.getContentNulls());
    }
}