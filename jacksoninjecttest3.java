package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the standard methods (toString, hashCode, equals) of the
 * {@link JacksonInject.Value} class.
 */
@DisplayName("JacksonInject.Value")
class JacksonInjectValueTest {

    @Nested
    @DisplayName("toString() method")
    class ToStringTest {

        @Test
        @DisplayName("should return a descriptive string for an empty value")
        void shouldCreateDescriptiveStringForEmptyValue() {
            assertEquals("JacksonInject.Value(id=null,useInput=null,optional=null)",
                    JacksonInject.Value.empty().toString());
        }

        @Test
        @DisplayName("should return a descriptive string for a configured value")
        void shouldCreateDescriptiveStringForConfiguredValue() {
            JacksonInject.Value configuredValue = JacksonInject.Value.construct("myId", true, false);
            assertEquals("JacksonInject.Value(id=myId,useInput=true,optional=false)",
                    configuredValue.toString());
        }
    }

    @Nested
    @DisplayName("hashCode() method")
    class HashCodeTest {

        @Test
        @DisplayName("should return the same hash code for equal objects")
        void shouldReturnSameHashCodeForEqualObjects() {
            JacksonInject.Value value1 = JacksonInject.Value.construct("id", true, true);
            JacksonInject.Value value2 = JacksonInject.Value.construct("id", true, true);

            assertEquals(value1, value2, "Precondition: objects must be equal.");
            assertEquals(value1.hashCode(), value2.hashCode());
        }

        @Test
        @DisplayName("of an empty value should not be zero")
        void hashCodeOfEmptyValueShouldNotBeZero() {
            // The specific hash code is not guaranteed, but a zero hash for an empty
            // instance is often a sign of a trivial or unimplemented hash function.
            assertNotEquals(0, JacksonInject.Value.empty().hashCode());
        }
    }

    @Nested
    @DisplayName("equals() method")
    class EqualsTest {

        private final JacksonInject.Value baseValue = JacksonInject.Value.construct("value", true, true);

        @Test
        @DisplayName("should be reflexive")
        void isReflexive() {
            assertEquals(baseValue, baseValue);
        }

        @Test
        @DisplayName("should be symmetric")
        void isSymmetric() {
            JacksonInject.Value equalValue = JacksonInject.Value.construct("value", true, true);
            assertEquals(baseValue, equalValue);
            assertEquals(equalValue, baseValue);
        }

        @Test
        @DisplayName("should be transitive")
        void isTransitive() {
            JacksonInject.Value value1 = JacksonInject.Value.construct("value", true, true);
            JacksonInject.Value value2 = JacksonInject.Value.construct("value", true, true);
            JacksonInject.Value value3 = JacksonInject.Value.construct("value", true, true);

            assertEquals(value1, value2);
            assertEquals(value2, value3);
            assertEquals(value1, value3);
        }

        @Test
        @DisplayName("should return false when compared to null")
        void returnsFalseForNull() {
            assertNotEquals(null, baseValue);
        }

        @Test
        @DisplayName("should return false when compared to a different type")
        @SuppressWarnings("unlikely-arg-type")
        void returnsFalseForDifferentType() {
            assertNotEquals("a string", baseValue);
        }

        @Test
        @DisplayName("should return true for two different empty instances")
        void emptyInstancesAreEqual() {
            assertEquals(JacksonInject.Value.empty(), JacksonInject.Value.empty());
        }

        @ParameterizedTest(name = "when {2} differs")
        @MethodSource("provideUnequalValuePairs")
        @DisplayName("should return false for non-equal objects")
        void returnsFalseForUnequalObjects(JacksonInject.Value value1, JacksonInject.Value value2, String reason) {
            assertNotEquals(value1, value2);
        }

        static Stream<Arguments> provideUnequalValuePairs() {
            JacksonInject.Value base = JacksonInject.Value.construct("value", true, true);
            return Stream.of(
                    Arguments.of(base, JacksonInject.Value.construct("different value", true, true), "id"),
                    Arguments.of(base, JacksonInject.Value.construct("value", false, true), "useInput"),
                    Arguments.of(base, JacksonInject.Value.construct("value", true, false), "optional"),
                    Arguments.of(base, JacksonInject.Value.construct(null, true, true), "id is null"),
                    Arguments.of(base, JacksonInject.Value.construct("value", null, true), "useInput is null"),
                    Arguments.of(base, JacksonInject.Value.construct("value", true, null), "optional is null")
            );
        }
    }
}