package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link JsonSetter.Value} class.
 */
public class JsonSetterValueTest {

    /**
     * Verifies that calling `withContentNulls()` with a null argument on the
     * singleton EMPTY instance does not create a new object.
     * <p>
     * This behavior is an important optimization to avoid unnecessary object allocation
     * when no actual configuration change is requested.
     */
    @Test
    public void withContentNulls_whenCalledWithNullOnEmptyInstance_shouldReturnSameInstance() {
        // Arrange: Start with the default, empty JsonSetter.Value instance.
        final JsonSetter.Value emptyInstance = JsonSetter.Value.EMPTY;

        // Act: Attempt to modify the instance with a null value.
        final JsonSetter.Value result = emptyInstance.withContentNulls(null);

        // Assert: The method should return the original EMPTY instance, not a new one.
        assertSame("Expected withContentNulls(null) on EMPTY to be a no-op and return the same instance",
                emptyInstance, result);
    }
}