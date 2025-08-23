package org.apache.commons.lang3.stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collector;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for the joining collectors in {@link LangCollectors}.
 * This focuses on collectors that join elements of any type, not just CharSequence.
 */
@DisplayName("LangCollectors.joining")
class LangCollectorsJoiningTest {

    /**
     * A simple fixture class to test joining of custom objects.
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
    @DisplayName("with delimiter, prefix, suffix, and toString function")
    class JoiningWithAllParametersTest {

        private final Collector<Object, ?, String> standardCollector = LangCollectors.joining("-", "<", ">", Objects::toString);

        @Test
        void shouldReturnOnlyPrefixAndSuffixForEmptyInput() {
            // Act
            final String result = LangCollectors.collect(standardCollector);

            // Assert
            assertEquals("<>", result);
        }

        @Test
        void shouldNotUseDelimiterForSingleElement() {
            // Act
            final String result = LangCollectors.collect(standardCollector, 1L);

            // Assert
            assertEquals("<1>", result);
        }

        @Test
        void shouldJoinMultipleLongsWithDelimiter() {
            // Act
            final String result = LangCollectors.collect(standardCollector, 1L, 2L, 3L);

            // Assert
            assertEquals("<1-2-3>", result);
        }

        @Test
        void shouldHandleNullElementsUsingDefaultToString() {
            // Act
            final String result = LangCollectors.collect(standardCollector, 1L, null, 3L);

            // Assert
            assertEquals("<1-null-3>", result, "A null element should be converted to the string 'null'");
        }

        @Test
        void shouldJoinCustomObjectsUsingTheirToStringMethod() {
            // Arrange
            final Fixture fixture1 = new Fixture(1);
            final Fixture fixture2 = new Fixture(2);

            // Act
            final String result = LangCollectors.collect(standardCollector, fixture1, fixture2);

            // Assert
            assertEquals("<1-2>", result);
        }

        @Test
        void shouldJoinStandardLibraryObjectsUsingTheirToStringMethod() {
            // Arrange
            final AtomicLong atomicLong1 = new AtomicLong(1);
            final AtomicLong atomicLong2 = new AtomicLong(2);

            // Act
            final String result = LangCollectors.collect(standardCollector, atomicLong1, atomicLong2);

            // Assert
            assertEquals("<1-2>", result);
        }

        @Test
        void shouldHandleNullsUsingCustomToStringFunction() {
            // Arrange
            final Function<Object, String> customToString = obj -> Objects.toString(obj, "NUL");
            final Collector<Object, ?, String> collectorWithCustomNull = LangCollectors.joining("-", "<", ">", customToString);

            // Act
            final String result = LangCollectors.collect(collectorWithCustomNull, 1L, null, 3L);

            // Assert
            assertEquals("<1-NUL-3>", result, "A null element should be converted using the provided function");
        }
    }
}