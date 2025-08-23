package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test suite focuses on the {@link JsonSetter.Value} class.
 */
// The original test class name `JsonSetter_ESTestTest26` is kept for consistency,
// but a more descriptive name like `JsonSetterValueTest` would be preferable in a real project.
public class JsonSetter_ESTestTest26 extends JsonSetter_ESTest_scaffolding {

    /**
     * Verifies that the readResolve() method, used during deserialization,
     * returns the same instance for a non-default {@code JsonSetter.Value}.
     * This ensures that the object's identity is preserved when it does not
     * match the canonical {@code EMPTY} instance.
     */
    @Test
    public void readResolveShouldReturnSameInstanceForNonDefaultValue() {
        // Arrange: Create a JsonSetter.Value instance that is not the default "empty" value.
        // A non-default contentNulls setting is used to achieve this.
        final Nulls valueNulls = Nulls.DEFAULT;
        final Nulls contentNulls = Nulls.SKIP;
        JsonSetter.Value originalValue = JsonSetter.Value.construct(valueNulls, contentNulls);

        // Act: Call readResolve(), which simulates the object being resolved during deserialization.
        JsonSetter.Value resolvedValue = (JsonSetter.Value) originalValue.readResolve();

        // Assert: For non-default values, readResolve should return the exact same instance.
        assertSame("For a non-default value, readResolve() should return the same instance.",
                originalValue, resolvedValue);

        // Additionally, verify that the state of the resolved object is correct.
        assertEquals("The valueNulls property should be preserved.",
                valueNulls, resolvedValue.getValueNulls());
        assertEquals("The contentNulls property should be preserved.",
                contentNulls, resolvedValue.getContentNulls());
    }
}