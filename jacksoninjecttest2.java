package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link JacksonInject.Value} class, focusing on its
 * creation from a {@link JacksonInject} annotation instance.
 */
@DisplayName("JacksonInject.Value Factory")
class JacksonInjectValueFromAnnotationTest {

    /**
     * A test fixture class containing fields with various {@link JacksonInject} annotations.
     */
    private static class AnnotatedBean {
        @JacksonInject(value = "inject", useInput = OptBoolean.FALSE, optional = OptBoolean.FALSE)
        public int fullyConfigured;

        @JacksonInject
        public int defaultAnnotation;

        @JacksonInject(optional = OptBoolean.TRUE)
        public int optionalOnly;
    }

    @Nested
    @DisplayName("from(JacksonInject) method")
    class FromAnnotation {

        @Test
        @DisplayName("should return an empty Value when given a null annotation")
        void from_withNullAnnotation_returnsEmptyValue() {
            // Act
            JacksonInject.Value value = JacksonInject.Value.from(null);

            // Assert
            assertSame(JacksonInject.Value.empty(), value, "A null annotation should result in the singleton empty Value instance.");
        }

        @Test
        @DisplayName("should create a configured Value from a fully populated annotation")
        void from_withFullyPopulatedAnnotation_createsConfiguredValue() throws Exception {
            // Arrange
            JacksonInject annotation = getAnnotation("fullyConfigured");

            // Act
            JacksonInject.Value value = JacksonInject.Value.from(annotation);

            // Assert
            assertAll("Value from fully configured annotation",
                    () -> assertEquals("inject", value.getId(), "ID should be extracted from the annotation's value"),
                    () -> assertEquals(Boolean.FALSE, value.getUseInput(), "'useInput' should be set to false"),
                    () -> assertEquals(Boolean.FALSE, value.getOptional(), "'optional' should be set to false"),
                    () -> assertNotEquals(JacksonInject.Value.empty(), value, "Value should not be equal to the empty instance"),
                    () -> assertEquals("JacksonInject.Value(id=inject,useInput=false,optional=false)", value.toString())
            );
        }

        @Test
        @DisplayName("should create a Value with null properties from a default annotation")
        void from_withDefaultAnnotation_createsValueWithNullProperties() throws Exception {
            // Arrange
            JacksonInject annotation = getAnnotation("defaultAnnotation");

            // Act
            JacksonInject.Value value = JacksonInject.Value.from(annotation);

            // Assert
            assertAll("Value from default annotation",
                    () -> assertNull(value.getId(), "ID should be null as it's not specified"),
                    () -> assertNull(value.getUseInput(), "'useInput' should be null to indicate default behavior"),
                    () -> assertNull(value.getOptional(), "'optional' should be null to indicate default behavior")
            );
            // A Value with all null properties is considered equivalent to the empty instance
            assertEquals(JacksonInject.Value.empty(), value);
        }

        @Test
        @DisplayName("should create a Value with optional=true from an annotation with only optional set")
        void from_withOptionalOnlyAnnotation_createsValueWithOptionalTrue() throws Exception {
            // Arrange
            JacksonInject annotation = getAnnotation("optionalOnly");

            // Act
            JacksonInject.Value value = JacksonInject.Value.from(annotation);

            // Assert
            assertAll("Value from optional-only annotation",
                    () -> assertNull(value.getId(), "ID should be null as it's not specified"),
                    () -> assertNull(value.getUseInput(), "'useInput' should be null to indicate default behavior"),
                    () -> assertEquals(Boolean.TRUE, value.getOptional(), "'optional' should be set to true")
            );
        }

        /**
         * Helper to reduce reflection boilerplate in tests.
         */
        private JacksonInject getAnnotation(String fieldName) throws NoSuchFieldException {
            Field field = AnnotatedBean.class.getField(fieldName);
            return field.getAnnotation(JacksonInject.class);
        }
    }
}