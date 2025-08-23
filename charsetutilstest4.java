package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link CharSetUtils#count(String, String...)}.
 */
@DisplayName("CharSetUtils.count")
class CharSetUtilsCountTest extends AbstractLangTest {

    /**
     * Provides test data for the count method.
     * Each argument set consists of: the input string, the character set, and the expected count.
     */
    private static Stream<Arguments> countTestData() {
        return Stream.of(
            // 1. Edge cases with null or empty inputs
            Arguments.of(null, "a-z", 0, "Null input string should result in 0"),
            Arguments.of("hello", null, 0, "Null character set should result in 0"),
            Arguments.of(null, null, 0, "Null input and null set should result in 0"),
            Arguments.of("", "a-z", 0, "Empty input string should result in 0"),
            Arguments.of("hello", "", 0, "Empty character set should result in 0"),
            Arguments.of("", "", 0, "Empty input and empty set should result in 0"),

            // 2. Standard cases with character ranges
            Arguments.of("hello", "a-e", 1, "Only 'e' is in the range 'a-e'"),
            Arguments.of("hello", "l-p", 3, "'l', 'l', and 'o' are in the range 'l-p'"),
            Arguments.of("hello", "z", 0, "No characters match the set 'z'"),
            Arguments.of("abacaba", "a", 4, "Count all occurrences of 'a'"),
            Arguments.of("abacaba", "b", 2, "Count all occurrences of 'b'")
        );
    }

    @DisplayName("should correctly count characters present in the given set")
    @ParameterizedTest(name = "[{index}] For input \"{0}\" and set \"{1}\", expected count is {2} because: {3}")
    @MethodSource("countTestData")
    void testCount(final String str, final String set, final int expectedCount, final String reason) {
        assertEquals(expectedCount, CharSetUtils.count(str, set));
    }
}