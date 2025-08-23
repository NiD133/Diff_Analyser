package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JsonSetter.Value} class, focusing on its configuration merging logic.
 */
public class JsonSetterValueTest {

    /**
     * Verifies that calling 'withOverrides' with a null argument returns the original
     * instance without any modifications. This confirms that a null override is correctly
     * handled as a no-op.
     */
    @Test
    public void withOverrides_whenGivenNull_shouldReturnSameInstance() {
        // Arrange: Create an initial JsonSetter.Value instance with specific settings.
        final Nulls nullsHandling = Nulls.AS_EMPTY;
        JsonSetter.Value initialValue = JsonSetter.Value.forValueNulls(nullsHandling, nullsHandling);

        // Act: Attempt to apply a null override.
        JsonSetter.Value resultValue = initialValue.withOverrides(null);

        // Assert: The method should return the exact same instance, not just an equal one.
        assertSame("Applying a null override should not create a new instance.", initialValue, resultValue);
        
        // For completeness, verify the properties remain unchanged.
        assertEquals(nullsHandling, resultValue.getValueNulls());
        assertEquals(nullsHandling, resultValue.getContentNulls());
    }
}