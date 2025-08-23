package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the standard methods (equals, hashCode, toString) of the {@link JsonSetter.Value} class.
 */
@DisplayName("JsonSetter.Value")
class JsonSetterValueTest {

    @Nested
    @DisplayName("standard method contracts")
    class StandardMethodTests {

        private final JsonSetter.Value EMPTY_VALUE = JsonSetter.Value.empty();

        @Test
        @DisplayName("toString() should produce a clear representation for the default instance")
        void toStringForDefaultInstanceIsCorrect() {
            // Arrange
            String expected = "JsonSetter.Value(valueNulls=DEFAULT,contentNulls=DEFAULT)";

            // Act & Assert
            assertEquals(expected, EMPTY_VALUE.toString());
        }

        @Test
        @DisplayName("hashCode() should be consistent for equal objects")
        void hashCodeIsConsistentForEqualObjects() {
            // Arrange: Create two separate but logically equal instances.
            // These will be distinct objects in memory but should be considered equal.
            JsonSetter.Value value1 = JsonSetter.Value.forValueNulls(Nulls.SKIP);
            JsonSetter.Value value2 = JsonSetter.Value.forValueNulls(Nulls.SKIP);

            // Assert
            // 1. The primary contract: equal objects must have the same hash code.
            assertEquals(value1.hashCode(), value2.hashCode(),
                    "Equal objects must have the same hash code.");

            // 2. A sanity check from the original test: hash code for the default instance should not be zero.
            assertNotEquals(0, EMPTY_VALUE.hashCode(),
                    "Default instance hash code should not be zero as a sanity check.");
        }

        @Test
        @DisplayName("equals() should adhere to its general contract")
        void equalsAdheresToContract() {
            // Arrange
            JsonSetter.Value valueA1 = JsonSetter.Value.construct(Nulls.SKIP, Nulls.DEFAULT);
            JsonSetter.Value valueA2 = JsonSetter.Value.construct(Nulls.SKIP, Nulls.DEFAULT);
            JsonSetter.Value valueB = JsonSetter.Value.construct(Nulls.FAIL, Nulls.DEFAULT);

            // 1. Reflexivity: An object must be equal to itself.
            assertEquals(EMPTY_VALUE, EMPTY_VALUE, "An object must be equal to itself.");
            assertEquals(valueA1, valueA1, "A non-empty object must be equal to itself.");

            // 2. Symmetry: If x.equals(y), then y.equals(x).
            assertEquals(valueA1, valueA2, "Objects with the same properties should be equal.");
            assertEquals(valueA2, valueA1, "equals() must be symmetric.");

            // 3. Inequality with a different Value instance
            assertNotEquals(valueA1, valueB, "Objects with different properties should not be equal.");
            assertNotEquals(valueB, valueA1);

            // 4. Inequality with null
            assertNotEquals(null, EMPTY_VALUE, "An object must not be equal to null.");

            // 5. Inequality with a different type
            assertNotEquals("a string", EMPTY_VALUE, "An object must not be equal to an instance of a different class.");
        }
    }
}