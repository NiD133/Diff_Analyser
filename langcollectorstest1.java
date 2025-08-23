package org.apache.commons.lang3.stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Collector;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link LangCollectors}.
 */
@DisplayName("LangCollectors")
class LangCollectorsTest {

    /**
     * Tests for the joining() collector overloads.
     */
    @Nested
    @DisplayName("joining collector")
    class JoiningTest {

        private static final Collector<Object, ?, String> JOINING_WITH_DASH_DELIMITER = LangCollectors.joining("-");

        /**
         * A helper method that centralizes the call to the collector under test.
         *
         * @param elements The objects to be joined.
         * @return The joined string.
         */
        private String joinWithDash(final Object... elements) {
            return LangCollectors.collect(JOINING_WITH_DASH_DELIMITER, elements);
        }

        static Stream<Arguments> joiningWithDelimiterTestCases() {
            return Stream.of(
                Arguments.of("should return empty string for no elements", "", new Object[]{}),
                Arguments.of("should return element as string for a single element", "1", new Object[]{"1"}),
                Arguments.of("should join two elements with delimiter", "1-2", new Object[]{"1", "2"}),
                Arguments.of("should join multiple elements with delimiter", "1-2-3", new Object[]{"1", "2", "3"}),
                Arguments.of("should treat null as the string 'null'", "1-null-3", new Object[]{"1", null, "3"})
            );
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("joiningWithDelimiterTestCases")
        @DisplayName("with a delimiter should correctly join elements")
        void testJoiningWithDelimiter(final String description, final String expected, final Object... elements) {
            final String result = joinWithDash(elements);
            assertEquals(expected, result);
        }
    }
}