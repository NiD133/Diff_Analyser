package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the factory methods of the {@link JsonSetter.Value} class.
 */
@DisplayName("JsonSetter.Value Factory Methods")
class JsonSetterValueFactoryTest {

    @Test
    @DisplayName("forContentNulls() should create an instance with specified contentNulls and default valueNulls")
    void forContentNullsShouldSetContentNullsOnly() {
        // Arrange
        final Nulls contentNullsSetting = Nulls.SET;

        // Act: Create a Value instance using the factory for content nulls.
        JsonSetter.Value result = JsonSetter.Value.forContentNulls(contentNullsSetting);

        // Assert: Verify that only contentNulls is set and valueNulls remains default.
        assertAll("A value from forContentNulls()",
            () -> assertEquals(contentNullsSetting, result.getContentNulls(),
                "getContentNulls() should return the specified value."),
            () -> assertEquals(contentNullsSetting, result.nonDefaultContentNulls(),
                "nonDefaultContentNulls() should also return the specified value."),
            () -> assertEquals(Nulls.DEFAULT, result.getValueNulls(),
                "getValueNulls() should remain as the default.")
        );
    }

    @Test
    @DisplayName("forValueNulls() should create an instance with specified valueNulls and default contentNulls")
    void forValueNullsShouldSetValueNullsOnly() {
        // Arrange
        final Nulls valueNullsSetting = Nulls.SKIP;

        // Act: Create a Value instance using the factory for value nulls.
        JsonSetter.Value result = JsonSetter.Value.forValueNulls(valueNullsSetting);

        // Assert: Verify that only valueNulls is set and contentNulls remains default.
        assertAll("A value from forValueNulls()",
            () -> assertEquals(valueNullsSetting, result.getValueNulls(),
                "getValueNulls() should return the specified value."),
            () -> assertEquals(valueNullsSetting, result.nonDefaultValueNulls(),
                "nonDefaultValueNulls() should also return the specified value."),
            () -> assertEquals(Nulls.DEFAULT, result.getContentNulls(),
                "getContentNulls() should remain as the default.")
        );
    }
}