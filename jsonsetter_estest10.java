package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link JsonSetter.Value} class, focusing on its immutability
 * and "wither" methods.
 */
public class JsonSetterValueTest {

    /**
     * Verifies that the {@code withContentNulls()} method creates a new
     * {@link JsonSetter.Value} instance when the value for content nulls is changed.
     * It also confirms that the original instance remains unmodified, preserving
     * its immutable nature.
     */
    @Test
    public void withContentNulls_shouldReturnNewInstance_whenValueIsChanged() {
        // Arrange: Create an initial instance with distinct values for valueNulls and contentNulls.
        JsonSetter.Value originalValue = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.SKIP);
        final Nulls newContentNulls = Nulls.DEFAULT;

        // Act: Call the "wither" method to create a modified instance.
        JsonSetter.Value updatedValue = originalValue.withContentNulls(newContentNulls);

        // Assert:
        // 1. A new object instance should be created.
        assertNotSame("A new instance should be returned because the value changed.", originalValue, updatedValue);

        // 2. The new instance should have the updated contentNulls and the original valueNulls.
        assertEquals("The updated instance should have the new contentNulls value.",
                newContentNulls, updatedValue.getContentNulls());
        assertEquals("The valueNulls property should be carried over to the new instance.",
                Nulls.DEFAULT, updatedValue.getValueNulls());

        // 3. The original instance must remain unchanged (verify immutability).
        assertEquals("The original instance's contentNulls should not be modified.",
                Nulls.SKIP, originalValue.getContentNulls());
    }
}