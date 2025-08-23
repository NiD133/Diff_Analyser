package org.apache.commons.lang3.stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LangCollectors#joining(CharSequence, CharSequence, CharSequence)}.
 */
@DisplayName("LangCollectors.joining(delimiter, prefix, suffix)")
class LangCollectorsJoiningTest {

    private static final String DELIMITER = "-";
    private static final String PREFIX = "<";
    private static final String SUFFIX = ">";

    /** The collector instance under test. */
    private static final Collector<Object, ?, String> JOINING_COLLECTOR =
        LangCollectors.joining(DELIMITER, PREFIX, SUFFIX);

    /** A simple fixture class for testing with custom objects. */
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

    @Test
    void shouldReturnOnlyPrefixAndSuffixForEmptyStream() {
        // Arrange
        final Stream<Object> emptyStream = Stream.of();
        final String expected = PREFIX + SUFFIX; // "<>"

        // Act
        final String result = emptyStream.collect(JOINING_COLLECTOR);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    void shouldWrapSingleElementWithPrefixAndSuffix() {
        // Arrange
        final Stream<Long> singleElementStream = Stream.of(1L);
        final String expected = PREFIX + "1" + SUFFIX; // "<1>"

        // Act
        final String result = singleElementStream.collect(JOINING_COLLECTOR);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    void shouldJoinMultipleElementsWithDelimiter() {
        // Arrange
        final Stream<Long> multipleElementsStream = Stream.of(1L, 2L, 3L);
        final String expected = PREFIX + "1" + DELIMITER + "2" + DELIMITER + "3" + SUFFIX; // "<1-2-3>"

        // Act
        final String result = multipleElementsStream.collect(JOINING_COLLECTOR);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    void shouldRepresentNullAsString() {
        // Arrange
        final Stream<Object> streamWithNull = Stream.of(1L, null, 3L);
        final String expected = PREFIX + "1" + DELIMITER + "null" + DELIMITER + "3" + SUFFIX; // "<1-null-3>"

        // Act
        final String result = streamWithNull.collect(JOINING_COLLECTOR);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    void shouldUseToStringMethodForAtomicLongElements() {
        // Arrange
        final Stream<AtomicLong> stream = Stream.of(new AtomicLong(1), new AtomicLong(2));
        final String expected = PREFIX + "1" + DELIMITER + "2" + SUFFIX; // "<1-2>"

        // Act
        final String result = stream.collect(JOINING_COLLECTOR);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    void shouldUseToStringMethodForCustomObjectElements() {
        // Arrange
        final Stream<Fixture> stream = Stream.of(new Fixture(1), new Fixture(2));
        final String expected = PREFIX + "1" + DELIMITER + "2" + SUFFIX; // "<1-2>"

        // Act
        final String result = stream.collect(JOINING_COLLECTOR);

        // Assert
        assertEquals(expected, result);
    }
}