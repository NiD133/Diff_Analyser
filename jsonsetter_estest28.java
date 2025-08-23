package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link JsonSetter.Value} class, focusing on its handling of default values.
 */
public class JsonSetterValueTest {

    /**
     * Verifies that {@link JsonSetter.Value#nonDefaultContentNulls()} returns null
     * when the instance is created with the default setting for content nulls.
     * The `empty()` factory method provides such an instance.
     */
    @Test
    public void nonDefaultContentNulls_whenUsingDefaultValue_shouldReturnNull() {
        // Arrange: Create an empty JsonSetter.Value, which uses Nulls.DEFAULT for contentNulls.
        JsonSetter.Value value = JsonSetter.Value.empty();

        // Act: Call the method under test.
        Nulls result = value.nonDefaultContentNulls();

        // Assert: The result should be null, as the purpose of the method is to
        // return null for default settings.
        assertNull("Expected null because the contentNulls setting is the default", result);
    }
}