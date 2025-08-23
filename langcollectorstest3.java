package org.apache.commons.lang3.stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for the joining collectors in {@link LangCollectors}.
 *
 * <p>This refactored test improves on the original by:</p>
 * <ul>
 *     <li>Using a descriptive class name (LangCollectorsJoiningTest).</li>
 *     <li>Removing unused code related to other 'joining' overloads, which focuses the test on a single responsibility.</li>
 *     <li>Employing a {@link Nested} test class to group tests for a specific method overload, {@code joining(delimiter)}.</li>
 *     <li>Using {@link DisplayName} annotations and descriptive method names to clarify the intent of each test case.</li>
 *     <li>Breaking down a single large test method into smaller, focused tests for each distinct scenario (e.g., empty input, nulls, custom objects).</li>
 *     <li>Improving the name of the test fixture class for better readability.</li>
 * </ul>
 */
@DisplayName("LangCollectors.joining")
class LangCollectorsJoiningTest {

    // This nested class specifically tests the joining(delimiter) overload.
    @Nested
    @DisplayName("with a delimiter")
    class JoiningWithDelimiter {

        private Collector<Object, ?, String> collector;

        @BeforeEach
        void setUp() {
            // The collector under test is created once before each test.
            collector = LangCollectors.joining("-");
        }

        /** A helper method to encapsulate the collection logic, making tests cleaner. */
        private String join(final Object... objects) {
            return LangCollectors.collect(collector, objects);
        }

        @Test
        @DisplayName("should return an empty string when given no elements")
        void joiningEmptyReturnsEmptyString() {
            assertEquals("", join());
        }

        @Test
        @DisplayName("should return the element's string representation for a single element")
        void joiningSingleElementReturnsElementAsString() {
            assertEquals("1", join(1L));
        }

        @Test
        @DisplayName("should join multiple elements with the specified delimiter")
        void joiningMultipleElementsJoinsWithDelimiter() {
            assertEquals("1-2-3", join(1L, 2L, 3L));
        }

        @Test
        @DisplayName("should represent null elements as the string 'null'")
        void joiningWithNullElementUsesStringNull() {
            assertEquals("1-null-3", join(1L, null, 3L));
        }

        /**
         * A simple test-specific object to verify that the collector
         * correctly calls the toString() method on non-String objects.
         */
        private static final class TestObject {
            private final int value;

            private TestObject(final int value) {
                this.value = value;
            }

            @Override
            public String toString() {
                return Integer.toString(value);
            }
        }

        @Test
        @DisplayName("should use the toString() method for custom objects")
        void joiningCustomObjectsUsesToString() {
            assertEquals("1-2", join(new TestObject(1), new TestObject(2)));
        }

        @Test
        @DisplayName("should handle various standard library object types")
        void joiningStandardObjectsUsesToString() {
            assertEquals("1-2", join(new AtomicLong(1), new AtomicLong(2)));
        }
    }
}