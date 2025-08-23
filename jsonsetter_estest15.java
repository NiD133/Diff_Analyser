package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Tests for the {@link JsonSetter.Value} class, focusing on its factory methods.
 */
public class JsonSetter_ESTestTest15 extends JsonSetter_ESTest_scaffolding {

    /**
     * Verifies that calling {@code withValueNulls(Nulls, Nulls)} with values that
     * do not change the object's state returns the original instance.
     * <p>
     * This is an important optimization for immutable objects to avoid unnecessary
     * object creation. The test confirms that providing the same {@code valueNulls}
     * and a {@code null} for {@code contentNulls} (which signifies no change)
     * results in the same object reference being returned.
     */
    @Test
    public void withValueNulls_whenNoEffectiveChange_shouldReturnSameInstance() {
        // Arrange: Create an initial JsonSetter.Value instance.
        final Nulls initialSetting = Nulls.DEFAULT;
        final JsonSetter.Value initialValue = JsonSetter.Value.construct(initialSetting, initialSetting);

        // Act: Call the 'wither' method with the same valueNulls and a null contentNulls.
        // A null contentNulls parameter indicates that its value should not be changed.
        final JsonSetter.Value result = initialValue.withValueNulls(initialSetting, null);

        // Assert: The method should return the original instance, not a new one.
        assertSame("Expected the same instance to be returned when no effective change is made.",
                initialValue, result);
    }
}