package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link JsonSetter.Value} class.
 */
@DisplayName("JsonSetter.Value")
class JsonSetterValueTest {

    // A helper class with a field annotated by @JsonSetter to be used in tests.
    private static class BeanWithSetterAnnotation {
        @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.SKIP)
        public int field;
    }

    @Nested
    @DisplayName("from() factory method")
    class FromFactoryMethodTest {

        @Test
        @DisplayName("should return an empty Value instance when the annotation is null")
        void from_givenNullAnnotation_returnsEmptyInstance() {
            // Act
            JsonSetter.Value result = JsonSetter.Value.from(null);

            // Assert
            assertSame(JsonSetter.Value.empty(), result,
                "from(null) should return the singleton empty Value instance.");
        }

        @Test
        @DisplayName("should create a Value instance with properties matching the annotation")
        void from_givenAnnotation_createsMatchingValue() throws NoSuchFieldException {
            // Arrange: Get a @JsonSetter annotation instance from the test bean's field.
            JsonSetter annotation = BeanWithSetterAnnotation.class
                    .getField("field")
                    .getAnnotation(JsonSetter.class);

            // Act: Create a Value instance from the annotation.
            JsonSetter.Value setterValue = JsonSetter.Value.from(annotation);

            // Assert: The properties of the created Value must match the annotation's properties.
            assertNotNull(setterValue);
            assertEquals(Nulls.FAIL, setterValue.getValueNulls(),
                "The valueNulls property should be correctly extracted from the annotation.");
            assertEquals(Nulls.SKIP, setterValue.getContentNulls(),
                "The contentNulls property should be correctly extracted from the annotation.");
        }
    }
}