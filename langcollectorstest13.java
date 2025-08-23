package org.apache.commons.lang3.stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LangCollectors#joining(CharSequence, CharSequence, CharSequence, java.util.function.Function)}.
 */
@DisplayName("LangCollectors.joining")
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
    @DisplayName("with delimiter, prefix, suffix, and mapper")
    class JoiningWithAllParametersTest {

        private static final Collector<Object, ?, String> JOINING_COLLECTOR =
            LangCollectors.joining("-", "<", ">", Objects::toString);

        private static final Collector<Object, ?, String> JOINING_COLLECTOR_WITH_NULL_TEXT =
            LangCollectors.joining("-", "<", ">", o -> Objects.toString(o, "NUL"));

        @Test
        @DisplayName("should return only prefix and suffix for an empty stream")
        void shouldReturnOnlyPrefixAndSuffixForEmptyStream() {
            // Act
            final String result = Stream.of().collect(JOINING_COLLECTOR);

            // Assert
            assertEquals("<>", result);
        }

        @Test
        @DisplayName("should not add a delimiter for a single element stream")
        void shouldNotAddDelimiterForSingleElement() {
            // Act
            final String result = Stream.of(1L).collect(JOINING_COLLECTOR);

            // Assert
            assertEquals("<1>", result);
        }

        @Test
        @DisplayName("should join multiple elements with the delimiter")
        void shouldJoinMultipleElementsWithDelimiter() {
            // Act
            final String result = Stream.of(1L, 2L, 3L).collect(JOINING_COLLECTOR);

            // Assert
            assertEquals("<1-2-3>", result);
        }

        @Test
        @DisplayName("should convert null elements to the string 'null' by default")
        void shouldConvertNullToDefaultString() {
            // Act
            final String result = Stream.of(1L, null, 3L).collect(JOINING_COLLECTOR);

            // Assert
            assertEquals("<1-null-3>", result);
        }

        @Test
        @DisplayName("should use the custom mapper for null elements when provided")
        void shouldUseCustomMapperForNull() {
            // Act
            final String result = Stream.of(1L, null, 3L).collect(JOINING_COLLECTOR_WITH_NULL_TEXT);

            // Assert
            assertEquals("<1-NUL-3>", result);
        }

        @Test
        @DisplayName("should join custom objects by calling their toString() method")
        void shouldJoinCustomObjects() {
            // Arrange
            final Stream<AtomicLong> atomicLongStream = Stream.of(new AtomicLong(1), new AtomicLong(2));
            final Stream<Fixture> fixtureStream = Stream.of(new Fixture(1), new Fixture(2));

            // Act
            final String atomicLongResult = atomicLongStream.collect(JOINING_COLLECTOR);
            final String fixtureResult = fixtureStream.collect(JOINING_COLLECTOR);

            // Assert
            assertEquals("<1-2>", atomicLongResult, "Should work with AtomicLong");
            assertEquals("<1-2>", fixtureResult, "Should work with custom Fixture class");
        }
    }
}