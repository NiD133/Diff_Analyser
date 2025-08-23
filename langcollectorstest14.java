package org.apache.commons.lang3.stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link LangCollectors#joining()}.
 */
@DisplayName("LangCollectors.joining() Test")
class LangCollectorsJoiningNoArgsTest {

    static Stream<Arguments> joiningNoArgsTestCases() {
        return Stream.of(
            Arguments.of("empty stream", Collections.emptyList(), ""),
            Arguments.of("single element stream", List.of("1"), "1"),
            Arguments.of("two element stream", List.of("1", "2"), "12"),
            Arguments.of("three element stream", List.of("1", "2", "3"), "123"),
            // List.of() does not allow nulls, so we use Arrays.asList() here.
            Arguments.of("stream with null element", Arrays.asList("1", null, "3"), "1null3")
        );
    }

    @ParameterizedTest(name = "given {0}, result is \"{2}\"")
    @MethodSource("joiningNoArgsTestCases")
    @DisplayName("should concatenate elements without a delimiter")
    void testJoiningWithNoArguments(final String testCaseName, final List<Object> input, final String expected) {
        // Act
        final String result = input.stream().collect(LangCollectors.joining());

        // Assert
        assertEquals(expected, result);
    }
}