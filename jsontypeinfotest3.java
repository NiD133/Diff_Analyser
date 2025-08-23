package com.fasterxml.jackson.annotation;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JsonTypeInfo.Value Mutator ('wither') Methods")
public class JsonTypeInfoTestTest3 {

    // This annotation is the source for the initial JsonTypeInfo.Value instance.
    // It defines the state before any mutator methods are called.
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, visible = true, defaultImpl = JsonTypeInfo.class, requireTypeIdForSubtypes = OptBoolean.TRUE)
    private final static class Anno1 {
    }

    private JsonTypeInfo.Value initialValue;

    @BeforeEach
    void setUp() {
        // Create a baseline Value instance from the annotation for each test.
        // This ensures tests are isolated and start from a known state.
        initialValue = JsonTypeInfo.Value.from(Anno1.class.getAnnotation(JsonTypeInfo.class));
    }

    @Test
    @DisplayName("should correctly construct initial Value from annotation")
    void testInitialState() {
        // Verify the initial state derived from Anno1.
        // The 'include' property defaults to PROPERTY.
        // The 'defaultImpl' of JsonTypeInfo.class is a marker for 'no default', so it becomes null.
        assertAll("Initial Value properties",
                () -> assertEquals(JsonTypeInfo.Id.CLASS, initialValue.getIdType()),
                () -> assertEquals(As.PROPERTY, initialValue.getInclusionType()),
                () -> assertNull(initialValue.getDefaultImpl()),
                () -> assertTrue(initialValue.getIdVisible()),
                () -> assertEquals("", initialValue.getPropertyName())
        );
    }

    @Nested
    @DisplayName("withIdType()")
    class WithIdTypeTests {

        @Test
        @DisplayName("should return the same instance if the Id type is unchanged")
        void shouldReturnSameInstanceForSameId() {
            // When
            JsonTypeInfo.Value result = initialValue.withIdType(JsonTypeInfo.Id.CLASS);

            // Then
            assertSame(initialValue, result, "Calling with the same value should be a no-op and return the same instance.");
        }

        @Test
        @DisplayName("should return a new instance with the updated Id type")
        void shouldReturnNewInstanceForDifferentId() {
            // Given
            JsonTypeInfo.Id newIdType = JsonTypeInfo.Id.MINIMAL_CLASS;

            // When
            JsonTypeInfo.Value updatedValue = initialValue.withIdType(newIdType);

            // Then
            assertNotSame(initialValue, updatedValue, "A new instance should be returned for a different value.");
            assertEquals(newIdType, updatedValue.getIdType(), "The new instance should have the updated Id type.");
            assertEquals(JsonTypeInfo.Id.CLASS, initialValue.getIdType(), "The original instance should remain unchanged.");
        }
    }

    @Nested
    @DisplayName("withInclusionType()")
    class WithInclusionTypeTests {

        @Test
        @DisplayName("should return the same instance if the inclusion type is unchanged")
        void shouldReturnSameInstanceForSameInclusionType() {
            // When
            JsonTypeInfo.Value result = initialValue.withInclusionType(As.PROPERTY);

            // Then
            assertSame(initialValue, result, "Calling with the same value should be a no-op and return the same instance.");
        }

        @Test
        @DisplayName("should return a new instance with the updated inclusion type")
        void shouldReturnNewInstanceForDifferentInclusionType() {
            // Given
            As newInclusionType = As.EXTERNAL_PROPERTY;

            // When
            JsonTypeInfo.Value updatedValue = initialValue.withInclusionType(newInclusionType);

            // Then
            assertNotSame(initialValue, updatedValue, "A new instance should be returned for a different value.");
            assertEquals(newInclusionType, updatedValue.getInclusionType(), "The new instance should have the updated inclusion type.");
            assertEquals(As.PROPERTY, initialValue.getInclusionType(), "The original instance should remain unchanged.");
        }
    }

    @Nested
    @DisplayName("withDefaultImpl()")
    class WithDefaultImplTests {

        @Test
        @DisplayName("should return the same instance if the default implementation is unchanged")
        void shouldReturnSameInstanceForSameDefaultImpl() {
            // Note: The initial defaultImpl is null because Anno1 uses JsonTypeInfo.class as a marker for no default.
            // When
            JsonTypeInfo.Value result = initialValue.withDefaultImpl(null);

            // Then
            assertSame(initialValue, result, "Calling with the same value (null) should be a no-op and return the same instance.");
        }

        @Test
        @DisplayName("should return a new instance with the updated default implementation")
        void shouldReturnNewInstanceForDifferentDefaultImpl() {
            // Given
            Class<?> newDefaultImpl = String.class;

            // When
            JsonTypeInfo.Value updatedValue = initialValue.withDefaultImpl(newDefaultImpl);

            // Then
            assertNotSame(initialValue, updatedValue, "A new instance should be returned for a different value.");
            assertEquals(newDefaultImpl, updatedValue.getDefaultImpl(), "The new instance should have the updated default implementation.");
            assertNull(initialValue.getDefaultImpl(), "The original instance should remain unchanged.");
        }
    }

    @Nested
    @DisplayName("withIdVisible()")
    class WithIdVisibleTests {

        @Test
        @DisplayName("should return the same instance if visibility is unchanged")
        void shouldReturnSameInstanceForSameVisibility() {
            // When
            JsonTypeInfo.Value result = initialValue.withIdVisible(true);

            // Then
            assertSame(initialValue, result, "Calling with the same value should be a no-op and return the same instance.");
        }

        @Test
        @DisplayName("should return a new instance with the updated visibility")
        void shouldReturnNewInstanceForDifferentVisibility() {
            // When
            JsonTypeInfo.Value updatedValue = initialValue.withIdVisible(false);

            // Then
            assertNotSame(initialValue, updatedValue, "A new instance should be returned for a different value.");
            assertFalse(updatedValue.getIdVisible(), "The new instance should have the updated visibility.");
            assertTrue(initialValue.getIdVisible(), "The original instance should remain unchanged.");
        }
    }

    @Nested
    @DisplayName("withPropertyName()")
    class WithPropertyNameTests {

        @Test
        @DisplayName("should return a new instance with the updated property name")
        void shouldReturnNewInstanceForNewPropertyName() {
            // Given
            String newPropertyName = "foobar";

            // When
            JsonTypeInfo.Value updatedValue = initialValue.withPropertyName(newPropertyName);

            // Then
            assertNotSame(initialValue, updatedValue, "A new instance should be returned for a different value.");
            assertEquals(newPropertyName, updatedValue.getPropertyName(), "The new instance should have the updated property name.");
            assertEquals("", initialValue.getPropertyName(), "The original instance should remain unchanged.");
        }
    }
}