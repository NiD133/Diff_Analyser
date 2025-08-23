package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test class focuses on the functionality of the {@link JsonSetter.Value} class.
 */
public class JsonSetter_ESTestTest13 {

    /**
     * Tests that the factory method {@link JsonSetter.Value#withValueNulls(Nulls, Nulls)}
     * correctly creates a new instance with both value and content null handling strategies updated.
     */
    @Test
    public void withValueNulls_shouldUpdateBothValueAndContentNulls() {
        // Arrange: Create an initial JsonSetter.Value with unspecified null handling.
        // Using construct(null, null) is a clearer way to achieve the state
        // that the original test created using mocks.
        JsonSetter.Value initialValue = JsonSetter.Value.construct(null, null);

        final Nulls desiredValueNulls = Nulls.DEFAULT;
        final Nulls desiredContentNulls = Nulls.SKIP;

        // Act: Create a new Value instance by applying the new null handling strategies.
        JsonSetter.Value updatedValue = initialValue.withValueNulls(desiredValueNulls, desiredContentNulls);

        // Assert: Verify that the new instance has the correctly updated properties.
        assertEquals("Value nulls should be updated",
                desiredValueNulls, updatedValue.getValueNulls());
        assertEquals("Content nulls should be updated",
                desiredContentNulls, updatedValue.getContentNulls());

        // Assert the behavior of the non-default accessor, making its purpose clear.
        // Since contentNulls is SKIP (not DEFAULT), it should be returned directly.
        assertEquals("nonDefaultContentNulls() should return the specific value when it's not DEFAULT",
                desiredContentNulls, updatedValue.nonDefaultContentNulls());
    }
}