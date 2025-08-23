package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests the complex negation syntax for the {@link CharSet#getInstance(String...)} factory method.
 *
 * <p>This test focuses on how the caret character (^) is parsed in various contexts,
 * including as a literal, a single-character negation, and as part of a negated range.</p>
 */
class CharSetGetInstanceNegationTest extends AbstractLangTest {

    /**
     * Provides test cases for CharSet construction with strings involving negation.
     * Each argument set consists of a descriptive name, the input string, and the expected CharRange array.
     *
     * @return a stream of arguments for the parameterized test.
     */
    static Stream<Arguments> negatedCharSetProvider() {
        return Stream.of(
            Arguments.of("A single caret is a literal character", "^",
                new CharRange[]{CharRange.is('^')}),

            Arguments.of("A double caret negates the caret character", "^^",
                new CharRange[]{CharRange.isNot('^')}),

            Arguments.of("A triple caret negates the caret and adds a literal caret", "^^^",
                new CharRange[]{CharRange.isNot('^'), CharRange.is('^')}),

            Arguments.of("A quadruple caret is a redundant double-negation of the caret", "^^^^",
                new CharRange[]{CharRange.isNot('^')}),

            Arguments.of("A caret not at the start is a literal", "a^",
                new CharRange[]{CharRange.is('a'), CharRange.is('^')}),

            Arguments.of("A caret can negate a character before a literal hyphen", "^a-",
                new CharRange[]{CharRange.isNot('a'), CharRange.is('-')}),

            // Note: The original test's expectation for "^^c-c" seems inconsistent with the
            // parser's typical behavior. This test preserves the original's expected outcome.
            Arguments.of("A double caret followed by a character and range", "^^c-c",
                new CharRange[]{CharRange.isNotIn('^', 'c')}),

            Arguments.of("A caret can start a negated range", "^c-^",
                new CharRange[]{CharRange.isNotIn('c', '^')}),

            Arguments.of("A negated range can be followed by a literal character", "^c-^d",
                new CharRange[]{CharRange.isNotIn('c', '^'), CharRange.is('d')}),

            Arguments.of("A double caret can be followed by a literal character", "^^-",
                new CharRange[]{CharRange.isNot('^'), CharRange.is('-')})
        );
    }

    @DisplayName("Creates correct CharSet from string with complex negation")
    @ParameterizedTest(name = "[{index}] {0}: input=\"{1}\"")
    @MethodSource("negatedCharSetProvider")
    void testGetInstanceWithComplexNegation(final String description, final String input, final CharRange[] expectedRanges) {
        // Act
        final CharSet charSet = CharSet.getInstance(input);
        final CharRange[] actualRanges = charSet.getCharRanges();

        // Assert
        assertEquals(expectedRanges.length, actualRanges.length, "Incorrect number of CharRange objects were created.");

        for (final CharRange expectedRange : expectedRanges) {
            assertTrue(ArrayUtils.contains(actualRanges, expectedRange),
                () -> "Expected range '" + expectedRange + "' not found for input: \"" + input + "\"");
        }
    }
}