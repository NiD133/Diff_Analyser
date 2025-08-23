package org.apache.commons.lang3.stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the joining collectors in {@link LangCollectors}, specifically focusing on
 * their behavior when collecting from a null array.
 */
class LangCollectorsJoiningTest {

    /**
     * Provides arguments for testing the various joining collectors. Each argument consists of:
     * 1. A descriptive name for the test case.
     * 2. The collector instance to be tested.
     * 3. The expected string result when collecting from a null array.
     *
     * @return a stream of arguments for the parameterized test.
     */
    private static Stream<Arguments> joiningCollectorProvider() {
        final Function<Object, String> nullToNulString = o -> Objects.toString(o, "NUL");

        return Stream.of(
            Arguments.of(
                "joining()",
                LangCollectors.joining(),
                ""
            ),
            Arguments.of(
                "joining(delimiter)",
                LangCollectors.joining("-"),
                ""
            ),
            Arguments.of(
                "joining(delimiter, prefix, suffix)",
                LangCollectors.joining("-", "<", ">"),
                "<>"
            ),
            Arguments.of(
                "joining(delimiter, prefix, suffix, toString)",
                LangCollectors.joining("-", "<", ">", nullToNulString),
                "<>"
            )
        );
    }

    @DisplayName("Collecting from a null array")
    @ParameterizedTest(name = "using {0} should result in \"{2}\"")
    @MethodSource("joiningCollectorProvider")
    void collectJoiningWithNullArrayShouldReturnEmptyOrPrefixSuffix(
            final String collectorName, final Collector<Object, ?, String> collector, final String expectedResult) {
        // The method under test, `LangCollectors.collect`, treats a null array as an empty stream.
        // For joining collectors, this means:
        // - Returning an empty string if no prefix/suffix are defined.
        // - Returning just the prefix and suffix if they are defined.

        // Act
        final String result = LangCollectors.collect(collector, (Object[]) null);

        // Assert
        assertEquals(expectedResult, result);
    }
}