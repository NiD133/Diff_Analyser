package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This test suite validates the functionality of the {@link JsonSetter} annotation
 * and its inner {@link JsonSetter.Value} class.
 * Tests are organized into nested classes to group related behaviors for better readability.
 */
@DisplayName("Tests for @JsonSetter and JsonSetter.Value")
public class JsonSetterTest {

    /**
     * Tests for the {@link JsonSetter.Value} class, which encapsulates configuration.
     */
    @Nested
    @DisplayName("JsonSetter.Value")
    class ValueTests {

        private final JsonSetter.Value EMPTY = JsonSetter.Value.empty();

        // A test-only class with a JsonSetter annotation for testing 'from()'
        private static class Bogus {
            @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.SKIP)
            public int field;
        }

        /**
         * Tests for factory methods that create JsonSetter.Value instances.
         */
        @Nested
        @DisplayName("Factory Methods")
        class FactoryTests {

            @Test
            @DisplayName("empty() should provide an instance with default values")
            void emptyInstance_shouldHaveDefaultValues() {
                // Assert
                assertEquals(Nulls.DEFAULT, EMPTY.getValueNulls());
                assertEquals(Nulls.DEFAULT, EMPTY.getContentNulls());
                assertEquals(JsonSetter.class, EMPTY.valueFor());

                // nonDefault... accessors should return null for default values
                assertNull(EMPTY.nonDefaultValueNulls());
                assertNull(EMPTY.nonDefaultContentNulls());
            }

            @Test
            @DisplayName("from(null) should return the empty instance")
            void from_givenNull_shouldReturnEmptyInstance() {
                // Act
                JsonSetter.Value result = JsonSetter.Value.from(null);

                // Assert
                assertSame(EMPTY, result);
            }

            @Test
            @DisplayName("from(annotation) should read annotation properties")
            void from_givenAnnotation_shouldCreateInstanceWithCorrectValues() throws Exception {
                // Arrange
                JsonSetter annotation = Bogus.class.getField("field").getAnnotation(JsonSetter.class);

                // Act
                JsonSetter.Value result = JsonSetter.Value.from(annotation);

                // Assert
                assertEquals(Nulls.FAIL, result.getValueNulls());
                assertEquals(Nulls.SKIP, result.getContentNulls());
            }

            @Test
            @DisplayName("construct(null, null) should return the empty instance")
            void construct_givenNulls_shouldReturnEmptyInstance() {
                // Act
                JsonSetter.Value result = JsonSetter.Value.construct(null, null);

                // Assert
                assertSame(EMPTY, result);
            }

            @Test
            @DisplayName("construct(...) should create an instance with specified values")
            void construct_shouldCreateInstanceWithSpecifiedValues() {
                // Arrange
                Nulls valueNulls = Nulls.FAIL;
                Nulls contentNulls = Nulls.SKIP;

                // Act
                JsonSetter.Value result = JsonSetter.Value.construct(valueNulls, contentNulls);

                // Assert
                assertEquals(valueNulls, result.getValueNulls());
                assertEquals(contentNulls, result.getContentNulls());
            }

            @Test
            @DisplayName("forValueNulls(...) should set only the valueNulls property")
            void forValueNulls_shouldSetOnlyValueNulls() {
                // Act
                JsonSetter.Value result = JsonSetter.Value.forValueNulls(Nulls.SKIP);

                // Assert
                assertEquals(Nulls.SKIP, result.getValueNulls());
                assertEquals(Nulls.DEFAULT, result.getContentNulls());
                assertEquals(Nulls.SKIP, result.nonDefaultValueNulls());
            }

            @Test
            @DisplayName("forContentNulls(...) should set only the contentNulls property")
            void forContentNulls_shouldSetOnlyContentNulls() {
                // Act
                JsonSetter.Value result = JsonSetter.Value.forContentNulls(Nulls.SET);

                // Assert
                assertEquals(Nulls.DEFAULT, result.getValueNulls());
                assertEquals(Nulls.SET, result.getContentNulls());
                assertEquals(Nulls.SET, result.nonDefaultContentNulls());
            }
        }

        /**
         * Tests for the immutable 'with...' methods that create modified instances.
         */
        @Nested
        @DisplayName("Instance 'with' Methods")
        class WithMethodsTests {

            @Test
            @DisplayName("with... methods should be chainable")
            void withMethods_shouldBeChainable() {
                // Act
                JsonSetter.Value result = EMPTY.withContentNulls(Nulls.SKIP)
                        .withValueNulls(Nulls.FAIL);

                // Assert
                assertEquals(Nulls.FAIL, result.getValueNulls());
                assertEquals(Nulls.SKIP, result.getContentNulls());
            }

            @Test
            @DisplayName("with... methods should return the same instance if the value is unchanged")
            void withMethods_whenValueIsUnchanged_shouldReturnSameInstance() {
                // Arrange
                JsonSetter.Value v1 = JsonSetter.Value.construct(Nulls.FAIL, Nulls.SKIP);

                // Act & Assert
                assertSame(v1, v1.withValueNulls(Nulls.FAIL));
                assertSame(v1, v1.withContentNulls(Nulls.SKIP));
                assertSame(EMPTY, EMPTY.withValueNulls(null, null));
            }

            @Test
            @DisplayName("withValueNulls(null, null) should reset the instance to empty")
            void withValueNulls_givenTwoNulls_shouldResetToEmpty() {
                // Arrange
                JsonSetter.Value v1 = JsonSetter.Value.construct(Nulls.FAIL, Nulls.SKIP);

                // Act
                JsonSetter.Value result = v1.withValueNulls(null, null);

                // Assert
                assertSame(EMPTY, result);
            }

            @Test
            @DisplayName("withOverrides() should correctly merge values from another instance")
            void withOverrides_shouldMergeValues() {
                // Arrange
                JsonSetter.Value base = JsonSetter.Value.construct(Nulls.FAIL, Nulls.DEFAULT);
                JsonSetter.Value overrides = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.SKIP);

                // Act
                JsonSetter.Value merged = base.withOverrides(overrides);

                // Assert: 'merged' should take non-default values from 'overrides'
                // and keep others from 'base'.
                assertEquals(Nulls.FAIL, merged.getValueNulls()); // from base
                assertEquals(Nulls.SKIP, merged.getContentNulls()); // from overrides
            }

            @Test
            @DisplayName("withOverrides() on an empty instance should be equal to the other instance")
            void withOverrides_onEmpty_shouldBeEquivalentToOther() {
                // Arrange
                JsonSetter.Value configured = JsonSetter.Value.construct(Nulls.FAIL, Nulls.SKIP);

                // Act
                JsonSetter.Value merged = EMPTY.withOverrides(configured);

                // Assert
                assertNotSame(configured, merged, "withOverrides should create a new instance");
                assertEquals(configured, merged);
            }
        }

        /**
         * Tests for standard Object methods: equals(), hashCode(), and toString().
         */
        @Nested
        @DisplayName("Standard Object Methods")
        class StandardObjectMethodsTests {

            @Test
            @DisplayName("toString() should return a clear representation")
            void toString_shouldReturnCorrectRepresentation() {
                // Assert
                assertEquals("JsonSetter.Value(valueNulls=DEFAULT,contentNulls=DEFAULT)", EMPTY.toString());
            }

            @Test
            @DisplayName("hashCode() should be consistent")
            void hashCode_shouldBeConsistent() {
                // Arrange
                JsonSetter.Value v1 = JsonSetter.Value.construct(Nulls.FAIL, Nulls.SKIP);
                JsonSetter.Value v2 = JsonSetter.Value.construct(Nulls.FAIL, Nulls.SKIP);

                // Assert
                assertNotEquals(0, EMPTY.hashCode(), "Hash code should be non-zero for default instance");
                assertEquals(v1.hashCode(), v2.hashCode(), "Hash code should be consistent for equal objects");
            }

            @Test
            @DisplayName("equals() should correctly compare instances")
            void equals_shouldWorkCorrectly() {
                // Arrange
                JsonSetter.Value v1 = JsonSetter.Value.construct(Nulls.FAIL, Nulls.SKIP);
                JsonSetter.Value v2 = JsonSetter.Value.construct(Nulls.FAIL, Nulls.SKIP);
                JsonSetter.Value v3 = JsonSetter.Value.construct(Nulls.FAIL, Nulls.SET);

                // Assert
                assertEquals(EMPTY, EMPTY);
                assertEquals(v1, v2);
                assertEquals(v2, v1);

                assertNotEquals(v1, v3);
                assertNotEquals(v1, EMPTY);

                // Comparison with null and other types
                assertNotEquals(null, EMPTY);
                assertNotEquals("xyz", EMPTY);
            }
        }
    }
}