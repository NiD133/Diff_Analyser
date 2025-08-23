package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link JsonSetter.Value} class, focusing on its merging and override logic.
 */
public class JsonSetterValueTest {

    /**
     * Tests that the {@code withOverrides} method correctly ignores an override
     * for a property when its value is {@code Nulls.DEFAULT}.
     * The base value's specific setting should be retained, and since no
     * effective change occurs, the method should return the original instance.
     */
    @Test
    public void withOverrides_whenOverrideIsDefault_shouldRetainBaseSetting() {
        // Arrange
        // 1. Define a base configuration with a specific setting (AS_EMPTY) for both properties.
        JsonSetter.Value baseSettings = JsonSetter.Value.construct(Nulls.AS_EMPTY, Nulls.AS_EMPTY);

        // 2. Define an override configuration where 'valueNulls' is DEFAULT.
        //    This DEFAULT should not override the specific 'AS_EMPTY' setting from the base.
        //    The 'contentNulls' is the same as the base, so it also causes no change.
        JsonSetter.Value overrideSettings = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.AS_EMPTY);

        // Act
        // 3. Apply the overrides to the base configuration.
        JsonSetter.Value result = baseSettings.withOverrides(overrideSettings);

        // Assert
        // 4. Verify that the original 'valueNulls' setting was retained.
        assertEquals("The specific valueNulls from the base should be kept.",
                Nulls.AS_EMPTY, result.getValueNulls());

        // 5. Verify that contentNulls is also unchanged.
        assertEquals("The contentNulls should be unchanged.",
                Nulls.AS_EMPTY, result.getContentNulls());

        // 6. Verify that because no effective overrides were applied, the method returned
        //    the original, unmodified instance.
        assertSame("Expected the same instance to be returned as no effective change occurred.",
                baseSettings, result);
    }
}