package org.apache.commons.lang3.stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Collector;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link LangCollectors#joining(CharSequence, CharSequence, CharSequence)}.
 */
class LangCollectorsTest {

    /**
     * Provides test cases for the joining collector with a delimiter, prefix, and suffix.
     * Each argument set consists of the expected result string and the input elements to be joined.
     *
     * @return a stream of arguments for the parameterized test.
     */
    private static Stream<Arguments> joiningWithDelimiterPrefixAndSuffixCases() {
        return Stream.of(
            // Test case: No elements
            Arguments.of("<>", new Object[]{}),
            // Test case: Single element
            Arguments.of("<1>", new Object[]{"1"}),
            // Test case: Multiple elements
            Arguments.of("<1-2-3>", new Object[]{"1", "2", "3"}),
            // Test case: Elements including null
            Arguments.of("<1-null-3>", new Object[]{"1", null, "3"})
        );
    }

    @ParameterizedTest
    @MethodSource("joiningWithDelimiterPrefixAndSuffixCases")
    void joiningWithDelimiterPrefixAndSuffix_shouldCorrectlyJoinElements(final String expectedResult, final Object... elements) {
        // Arrange: Create a collector that joins objects with a hyphen,
        // enclosed in angle brackets.
        final Collector<Object, ?, String> collector = LangCollectors.joining("-", "<", ">");

        // Act: Collect the elements into a string.
        final String actualResult = LangCollectors.collect(collector, elements);

        // Assert: The resulting string should match the expected output.
        assertEquals(expectedResult, actualResult);
    }
}