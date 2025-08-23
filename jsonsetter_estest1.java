package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link JsonSetter.Value} class, focusing on its factory and merging logic.
 */
public class JsonSetterValueTest {

    @Test
    public void merge_whenBaseIsNull_shouldReturnOverrides() {
        // Arrange: Create an 'overrides' value and a null 'base' value.
        // The 'base' represents existing settings, and 'overrides' represents new settings.
        JsonSetter.Value base = null;
        JsonSetter.Value overrides = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY, Nulls.AS_EMPTY);

        // Act: Merge the null base with the overrides.
        JsonSetter.Value result = JsonSetter.Value.merge(base, overrides);

        // Assert: The result should be the 'overrides' instance itself, as there was no base to merge with.
        assertSame("When the base is null, merge should return the overrides instance directly", overrides, result);

        // We can also verify the properties for clarity.
        assertEquals(Nulls.AS_EMPTY, result.getValueNulls());
        assertEquals(Nulls.AS_EMPTY, result.getContentNulls());
    }
}