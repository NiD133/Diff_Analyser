package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class contains tests for the {@link JsonSetter.Value} class, focusing on its
 * "wither" methods that create modified instances.
 */
public class JsonSetterValueTest {

    /**
     * Tests that the two-argument `withValueNulls` method retains the original
     * `valueNulls` setting if the `valueNulls` argument provided is null.
     * It also verifies that if no properties are changed, the original instance is returned,
     * adhering to the expected behavior of an immutable object.
     */
    @Test
    public void withValueNulls_shouldNotChangeValueNulls_whenGivenNull() {
        // Arrange: Create an initial JsonSetter.Value with DEFAULT settings for both properties.
        final Nulls initialSetting = Nulls.DEFAULT;
        JsonSetter.Value initialInstance = JsonSetter.Value.construct(initialSetting, initialSetting);

        // Act: Call the "wither" method, passing null for the valueNulls parameter
        // and the original value for the contentNulls parameter.
        JsonSetter.Value resultInstance = initialInstance.withValueNulls(null, initialSetting);

        // Assert: Since no properties were effectively changed, the method should return
        // the original instance, not a new one.
        assertSame("Should return the same instance if no changes are made",
                initialInstance, resultInstance);

        // For completeness, also verify that the properties themselves are unchanged.
        assertEquals("Value nulls should remain unchanged",
                initialSetting, resultInstance.getValueNulls());
        assertEquals("Content nulls should remain unchanged",
                initialSetting, resultInstance.getContentNulls());
    }
}