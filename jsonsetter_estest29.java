package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test suite focuses on the {@link JsonSetter.Value} class,
 * verifying the behavior of its factory and accessor methods.
 */
public class JsonSetterValueTest {

    /**
     * Tests that the {@code JsonSetter.Value.construct(Nulls, Nulls)} factory method
     * correctly creates an instance with the specified settings for both value nulls and content nulls.
     */
    @Test
    public void shouldConstructValueWithSpecifiedNullsSettings() {
        // Arrange: Define the expected settings for null handling.
        final Nulls expectedValueNulls = Nulls.SET;
        final Nulls expectedContentNulls = Nulls.SET;

        // Act: Construct a JsonSetter.Value instance with the specified settings.
        JsonSetter.Value jsonSetterValue = JsonSetter.Value.construct(expectedValueNulls, expectedContentNulls);

        // Assert: Verify that the created instance holds the correct values.
        assertEquals("The 'valueNulls' property should match the constructor argument.",
                expectedValueNulls, jsonSetterValue.getValueNulls());
        assertEquals("The 'contentNulls' property should match the constructor argument.",
                expectedContentNulls, jsonSetterValue.getContentNulls());
    }
}