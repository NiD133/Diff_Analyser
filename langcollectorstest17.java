package org.apache.commons.lang3.stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for the joining collectors in {@link LangCollectors}.
 */
@DisplayName("Tests for LangCollectors")
class LangCollectorsJoiningTest {

    @Nested
    @DisplayName("joining(delimiter, prefix, suffix, toString)")
    class JoiningWithFourArguments {

        private final Collector<Object, ?, String> defaultCollector =
            LangCollectors.joining("-", "<", ">", Objects::toString);

        @Test
        @DisplayName("should return only prefix and suffix for an empty stream")
        void testEmptyStream() {
            // When
            final String result = Stream.of().collect(defaultCollector);

            // Then
            assertEquals("<>", result);
        }

        @Test
        @DisplayName("should wrap a single element in prefix and suffix")
        void testSingleElementStream() {
            // When
            final String result = Stream.of("1").collect(defaultCollector);

            // Then
            assertEquals("<1>", result);
        }

        @Test
        @DisplayName("should join multiple elements with the delimiter")
        void testMultipleElementsStream() {
            // When
            final String result = Stream.of("1", "2", "3").collect(defaultCollector);

            // Then
            assertEquals("<1-2-3>", result);
        }

        @Test
        @DisplayName("should convert null elements to the string 'null' by default")
        void testStreamWithNull() {
            // When
            final String result = Stream.of("1", null, "3").collect(defaultCollector);

            // Then
            assertEquals("<1-null-3>", result);
        }

        @Test
        @DisplayName("should use a custom mapping function for null elements")
        void testStreamWithNullAndCustomMapper() {
            // Given a collector that maps nulls to "NUL"
            final Collector<Object, ?, String> collectorWithCustomNullHandler =
                LangCollectors.joining("-", "<", ">", obj -> Objects.toString(obj, "NUL"));

            // When
            final String result = Stream.of("1", null, "3").collect(collectorWithCustomNullHandler);

            // Then
            assertEquals("<1-NUL-3>", result);
        }
    }
}