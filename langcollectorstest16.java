package org.apache.commons.lang3.stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Collector;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LangCollectors#joining(CharSequence, CharSequence, CharSequence)}.
 */
@DisplayName("LangCollectors.joining")
class LangCollectorsJoiningTest {

    @Nested
    @DisplayName("with delimiter, prefix, and suffix")
    class JoiningWithDelimiterPrefixAndSuffix {

        private final Collector<Object, ?, String> collector = LangCollectors.joining("-", "<", ">");

        @Test
        @DisplayName("should return only prefix and suffix for an empty stream")
        void onEmptyStream_shouldReturnPrefixAndSuffix() {
            // Given an empty stream
            final Stream<String> stream = Stream.of();

            // When collecting
            final String result = stream.collect(collector);

            // Then the result is just the prefix and suffix
            assertEquals("<>", result);
        }

        @Test
        @DisplayName("should wrap a single element in prefix and suffix")
        void onSingleElementStream_shouldWrapElementInPrefixAndSuffix() {
            // Given a stream with one element
            final Stream<String> stream = Stream.of("1");

            // When collecting
            final String result = stream.collect(collector);

            // Then the element is enclosed in the prefix and suffix
            assertEquals("<1>", result);
        }

        @Test
        @DisplayName("should join multiple elements with the delimiter")
        void onMultipleElementStream_shouldJoinWithDelimiter() {
            // Given a stream with multiple elements
            final Stream<String> stream = Stream.of("1", "2", "3");

            // When collecting
            final String result = stream.collect(collector);

            // Then the elements are joined by the delimiter and enclosed
            assertEquals("<1-2-3>", result);
        }

        @Test
        @DisplayName("should convert null elements to the string 'null'")
        void onStreamWithNull_shouldConvertNullToString() {
            // Given a stream containing a null element
            final Stream<String> stream = Stream.of("1", null, "3");

            // When collecting
            final String result = stream.collect(collector);

            // Then the null is converted to "null" in the output
            assertEquals("<1-null-3>", result);
        }
    }
}