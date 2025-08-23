package org.apache.commons.lang3.stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for the joining collectors in {@link LangCollectors}.
 */
@DisplayName("Tests for LangCollectors.joining")
class LangCollectorsJoiningTest {

    /**
     * A simple fixture class to test joining with custom objects.
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

    @Nested
    @DisplayName("joining(delimiter)")
    class JoiningWithDelimiter {

        private static final Collector<Object, ?, String> JOINING_WITH_DASH = LangCollectors.joining("-");

        @Test
        @DisplayName("should return an empty string for an empty stream")
        void withEmptyStream_shouldReturnAnEmptyString() {
            final String result = Stream.of().collect(JOINING_WITH_DASH);
            assertEquals("", result);
        }

        @Test
        @DisplayName("should return the element's string representation for a single-element stream")
        void withSingleElement_shouldReturnElementString() {
            final String result = Stream.of(1L).collect(JOINING_WITH_DASH);
            assertEquals("1", result);
        }

        @Test
        @DisplayName("should join multiple elements with the delimiter")
        void withMultipleElements_shouldJoinWithDelimiter() {
            final String result = Stream.of(1L, 2L, 3L).collect(JOINING_WITH_DASH);
            assertEquals("1-2-3", result);
        }

        @Test
        @DisplayName("should represent null elements as the string 'null'")
        void withNullElement_shouldRepresentAsNullString() {
            final String result = Stream.of(1L, null, 3L).collect(JOINING_WITH_DASH);
            assertEquals("1-null-3", result);
        }

        @Test
        @DisplayName("should use toString() for standard library objects")
        void withStandardObjects_shouldUseToString() {
            final Stream<AtomicLong> stream = Stream.of(new AtomicLong(1), new AtomicLong(2));
            final String result = stream.collect(JOINING_WITH_DASH);
            assertEquals("1-2", result);
        }

        @Test
        @DisplayName("should use toString() for custom objects")
        void withCustomObjects_shouldUseToString() {
            final Stream<Fixture> stream = Stream.of(new Fixture(1), new Fixture(2));
            final String result = stream.collect(JOINING_WITH_DASH);
            assertEquals("1-2", result);
        }
    }
}