package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Tests for the {@link JsonSetter.Value} class, focusing on its configuration merging logic.
 */
public class JsonSetterValueTest {

    /**
     * This test verifies an optimization in the {@code JsonSetter.Value.merge()} method.
     * When the 'overrides' instance provides no new information, the method should
     * return the original 'base' instance instead of creating a new one.
     *
     * <p>An override is considered redundant if its properties are either:
     * <ul>
     *     <li>The same as the corresponding property in the base.</li>
     *     <li>Set to {@link Nulls#DEFAULT}, which signifies "do not override".</li>
     * </ul>
     */
    @Test
    public void mergeShouldReturnBaseInstanceWhenOverridesAreRedundant() {
        // Arrange: Set up a base configuration and a redundant override configuration.

        // 1. A base configuration with specific non-default settings.
        final JsonSetter.Value baseValue = new JsonSetter.Value(Nulls.FAIL, Nulls.FAIL);

        // 2. An override configuration that is "redundant":
        //    - Its 'valueNulls' (FAIL) is the same as the base's.
        //    - Its 'contentNulls' is DEFAULT, meaning it should not override the base's setting.
        final JsonSetter.Value redundantOverrides = JsonSetter.Value.forValueNulls(Nulls.FAIL);

        // Act: Perform the merge operation.
        final JsonSetter.Value mergedValue = JsonSetter.Value.merge(baseValue, redundantOverrides);

        // Assert: The result should be the exact same instance as the base, not a new object.
        // This confirms the optimization is working as expected.
        assertSame("Expected merge to return the original base instance for redundant overrides",
                baseValue, mergedValue);

        // As a secondary check, confirm the final properties of the merged value are correct.
        assertEquals("valueNulls should remain FAIL", Nulls.FAIL, mergedValue.getValueNulls());
        assertEquals("contentNulls should remain FAIL", Nulls.FAIL, mergedValue.getContentNulls());
    }
}