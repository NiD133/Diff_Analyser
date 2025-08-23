package org.apache.commons.lang3.stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collector;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for the joining methods in {@link LangCollectors}.
 */
@DisplayName("Tests for LangCollectors.joining")
class LangCollectorsJoiningTest {

    /**
     * A simple fixture class for testing with custom objects.
     * Its toString() method returns the string representation of its value.
     */
    private static final class Fixture {
        private final int value;

        private Fixture(final int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return Integer.toString(value);
        }
    }

    /**
     * Tests for the {@link LangCollectors#joining(CharSequence, CharSequence, CharSequence)} overload.
     */
    @Nested
    @DisplayName("using joining(delimiter, prefix, suffix)")
    class JoiningWithDelimiterPrefixAndSuffix {

        private static final Collector<Object, ?, String> JOINING_COLLECTOR =
            LangCollectors.joining("-", "<", ">");

        /**
         * A helper to invoke the method under test, LangCollectors.collect(), with our specific collector.
         * This keeps the test cases clean and focused on the inputs and expected outputs.
         */
        private String join(final Object... objects) {
            return LangCollectors.collect(JOINING_COLLECTOR, objects);
        }

        @Test
        @DisplayName("should return only the prefix and suffix for an empty collection")
        void shouldReturnOnlyPrefixAndSuffixForEmptyInput() {
            assertEquals("<>", join());
        }

        @Test
        @DisplayName("should not use the delimiter for a single element")
        void shouldNotUseDelimiterForSingleElement() {
            assertEquals("<1>", join(1L));
        }

        @Test
        @DisplayName("should join multiple elements with the delimiter")
        void shouldJoinMultipleElementsWithDelimiter() {
            assertEquals("<1-2-3>", join(1L, 2L, 3L));
        }

        @Test
        @DisplayName("should convert a null element to the literal string 'null'")
        void shouldConvertNullElementToString() {
            // This behavior is inherited from the underlying java.util.StringJoiner.
            assertEquals("<1-null-3>", join(1L, null, 3L));
        }

        @Test
        @DisplayName("should join various object types using their toString() method")
        void shouldJoinDifferentObjectTypes() {
            final Fixture fixture = new Fixture(1);
            final AtomicLong atomicLong = new AtomicLong(2);
            assertEquals("<1-2>", join(fixture, atomicLong));
        }
    }
}