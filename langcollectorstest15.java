package org.apache.commons.lang3.stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Collector;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for the joining collectors in {@link LangCollectors}.
 */
@DisplayName("LangCollectors.joining")
class LangCollectorsJoiningTest {

    @Nested
    @DisplayName("when using joining(delimiter)")
    class JoiningWithDelimiter {

        private final Collector<Object, ?, String> joiningWithHyphen = LangCollectors.joining("-");

        @Test
        @DisplayName("should return an empty string for an empty stream")
        void shouldReturnEmptyStringForEmptyStream() {
            // Arrange
            final Stream<String> emptyStream = Stream.empty();

            // Act
            final String result = emptyStream.collect(joiningWithHyphen);

            // Assert
            assertEquals("", result);
        }

        @Test
        @DisplayName("should return the element itself for a single-element stream")
        void shouldReturnElementForSingleElementStream() {
            // Arrange
            final Stream<String> singleElementStream = Stream.of("item");

            // Act
            final String result = singleElementStream.collect(joiningWithHyphen);

            // Assert
            assertEquals("item", result);
        }

        @Test
        @DisplayName("should join multiple elements with the delimiter")
        void shouldJoinMultipleElementsWithDelimiter() {
            // Arrange
            final Stream<String> stream = Stream.of("a", "b", "c");

            // Act
            final String result = stream.collect(joiningWithHyphen);

            // Assert
            assertEquals("a-b-c", result);
        }

        @Test
        @DisplayName("should treat null elements as the string 'null'")
        void shouldTreatNullAsLiteralString() {
            // Arrange
            final Stream<Object> streamWithNull = Stream.of("a", null, "c");

            // Act
            final String result = streamWithNull.collect(joiningWithHyphen);

            // Assert
            assertEquals("a-null-c", result);
        }
    }
}