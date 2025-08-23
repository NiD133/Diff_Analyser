package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link JsonSetter.Value} class, focusing on its factory methods and default states.
 */
@DisplayName("JsonSetter.Value")
class JsonSetterValueTest {

    @Test
    @DisplayName("empty() should create an instance with default values")
    void emptyInstanceShouldHaveDefaultValues() {
        // Arrange: Obtain the default, "empty" instance.
        final JsonSetter.Value emptyValue = JsonSetter.Value.empty();

        // Act & Assert: Verify that the properties of the empty instance reflect the default configuration.
        assertAll("Properties of an empty JsonSetter.Value",
            () -> assertEquals(Nulls.DEFAULT, emptyValue.getValueNulls(),
                    "Default for 'valueNulls' should be Nulls.DEFAULT"),
            () -> assertEquals(Nulls.DEFAULT, emptyValue.getContentNulls(),
                    "Default for 'contentNulls' should be Nulls.DEFAULT"),
            () -> assertNull(emptyValue.nonDefaultValueNulls(),
                    "nonDefaultValueNulls() should be null when the value is default"),
            () -> assertNull(emptyValue.nonDefaultContentNulls(),
                    "nonDefaultContentNulls() should be null when the value is default"),
            () -> assertEquals(JsonSetter.class, emptyValue.valueFor(),
                    "valueFor() should return the JsonSetter annotation class")
        );
    }
}