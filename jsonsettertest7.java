package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JsonSetter.Value Immutability and 'with' Methods")
class JsonSetterTestTest7 {

    // The 'Bogus' inner class from the original test was unused and has been removed for clarity.

    @Nested
    @DisplayName("withContentNulls() method")
    class WithContentNullsTest {

        @Test
        @DisplayName("should return a new instance with updated content nulls")
        void withContentNullsShouldCreateNewInstanceWithUpdatedValue() {
            // Arrange
            final JsonSetter.Value initialValue = JsonSetter.Value.empty();

            // Act
            JsonSetter.Value updatedValue = initialValue.withContentNulls(Nulls.FAIL);

            // Assert
            assertNotSame(initialValue, updatedValue, "A new instance should be created for a new value");
            assertEquals(Nulls.FAIL, updatedValue.getContentNulls(), "ContentNulls should be updated");
            assertEquals(Nulls.DEFAULT, updatedValue.getValueNulls(), "ValueNulls should remain unchanged");
        }

        @Test
        @DisplayName("should return the same instance if the value is unchanged")
        void withContentNullsShouldReturnSameInstanceWhenValueIsUnchanged() {
            // Arrange
            final JsonSetter.Value initialValue = JsonSetter.Value.forContentNulls(Nulls.FAIL);

            // Act
            JsonSetter.Value updatedValue = initialValue.withContentNulls(Nulls.FAIL);

            // Assert
            assertSame(initialValue, updatedValue, "The same instance should be returned if the value doesn't change");
        }

        @Test
        @DisplayName("should return the same instance when called with null")
        void withContentNullsShouldReturnSameInstanceWhenCalledWithNull() {
            // Arrange
            final JsonSetter.Value initialValue = JsonSetter.Value.empty();

            // Act
            JsonSetter.Value result = initialValue.withContentNulls(null);

            // Assert
            assertSame(initialValue, result, "Calling with null should be a no-op and return the same instance");
        }
    }

    @Nested
    @DisplayName("withValueNulls() method")
    class WithValueNullsTest {

        @Test
        @DisplayName("should create a new, unequal instance with updated value nulls")
        void withValueNullsShouldCreateNewUnequalInstance() {
            // Arrange
            final JsonSetter.Value initialValue = JsonSetter.Value.forContentNulls(Nulls.FAIL);

            // Act
            JsonSetter.Value updatedValue = initialValue.withValueNulls(Nulls.SKIP);

            // Assert
            assertNotSame(initialValue, updatedValue, "A new instance should be created");
            assertNotEquals(initialValue, updatedValue, "Instances with different values should not be equal");
            assertEquals(Nulls.SKIP, updatedValue.getValueNulls(), "ValueNulls should be updated");
            assertEquals(Nulls.FAIL, updatedValue.getContentNulls(), "ContentNulls should remain unchanged");
        }

        @Test
        @DisplayName("overload with two nulls should reset both properties to default")
        void withValueNullsWithTwoNullsShouldResetToDefaults() {
            // Arrange
            final JsonSetter.Value initialValue = JsonSetter.Value.construct(Nulls.FAIL, Nulls.SKIP);

            // Act
            JsonSetter.Value resetValue = initialValue.withValueNulls(null, null);

            // Assert
            assertNotSame(initialValue, resetValue, "A new instance should be created when resetting");
            assertEquals(JsonSetter.Value.empty(), resetValue, "The new instance should be equal to the empty/default instance");
            assertAll("Both properties should be reset to DEFAULT",
                () -> assertEquals(Nulls.DEFAULT, resetValue.getValueNulls()),
                () -> assertEquals(Nulls.DEFAULT, resetValue.getContentNulls())
            );
        }

        @Test
        @DisplayName("overload with two nulls on a default instance should return the same instance")
        void withValueNullsWithTwoNullsOnDefaultInstanceShouldReturnSameInstance() {
            // Arrange
            final JsonSetter.Value initialValue = JsonSetter.Value.empty();

            // Act
            JsonSetter.Value result = initialValue.withValueNulls(null, null);

            // Assert
            assertSame(initialValue, result, "Resetting an already-default instance should be a no-op");
        }
    }

    @Nested
    @DisplayName("withOverrides() method")
    class WithOverridesTest {

        @Test
        @DisplayName("should merge properties, creating a new instance that is equal to the override")
        void withOverridesShouldMergeProperties() {
            // Arrange
            final JsonSetter.Value baseValue = JsonSetter.Value.empty();
            final JsonSetter.Value overrideValue = JsonSetter.Value.construct(Nulls.SKIP, Nulls.FAIL);

            // Act
            JsonSetter.Value mergedValue = baseValue.withOverrides(overrideValue);

            // Assert
            assertNotSame(overrideValue, mergedValue, "A new instance should be created by the merge operation");
            assertEquals(overrideValue, mergedValue, "The merged instance should be equal to the override instance");
            assertAll("Merged value should have properties from the override",
                () -> assertEquals(Nulls.SKIP, mergedValue.getValueNulls()),
                () -> assertEquals(Nulls.FAIL, mergedValue.getContentNulls())
            );
        }
    }
}